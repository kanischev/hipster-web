package controllers

import java.time.LocalDateTime

import _root_.security.{AppEnv, AppUser}
import _root_.services.{AuthTokenService, UserAccountService}
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{Clock, Credentials, PasswordHasherRegistry, PasswordInfo}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.typesafe.scalalogging.LazyLogging
import forms._
import forms.security.{ChangePasswordForm, ForgotPasswordForm, SignInForm, SignUpForm}
import io.swagger.annotations._
import javax.inject.Inject
import models.Tables.UserAccountRow
import play.api.Configuration
import play.api.i18n.{I18nSupport, Messages}
import play.api.libs.json.Json
import play.api.libs.mailer.MailerClient
import play.api.mvc._
import utils.Formats._
import utils.Metadata

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future}

@Api("Security API")
class SecurityController @Inject() (
                                     components: ControllerComponents,
                                     silhouette: Silhouette[AppEnv],
                                     authInfoRepository: AuthInfoRepository,
                                     authTokenService: AuthTokenService,
                                     userService: UserAccountService,
                                     credentialsProvider: CredentialsProvider,
                                     configuration: Configuration,
                                     passwordHasherRegistry: PasswordHasherRegistry,
                                     mailerClient: MailerClient,
                                     clock: Clock
                                   )(
                                      implicit assets: AssetsFinder,
                                      ex: ExecutionContext
                                   )
  extends AbstractController(components)
    with I18nSupport
    with LazyLogging {

  /**
    * Logs user in with silhouette context. Stores authentication info in Store, provided by userService
    * (Data base by default)
    *
    * @return
    */
  @ApiOperation(
    value = "Log user in",
    httpMethod = "POST"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      required = true,
      paramType = "body",
      dataTypeClass = classOf[SignInForm.Data],
      value = "Credentials"
    )))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "User successfully signed in"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "DB connection error")
  ))
  def login = silhouette.UnsecuredAction.async {
    implicit request: Request[AnyContent] =>
      logger.debug(s"Processing user login request for ${request.attrs}")
      SignInForm.form.bindFromRequest.fold(
        form => Future.successful(
          BadRequest(
            Json.toJson(FormSubmissionError(
              "login.parameters.incorrect",
              form.errors,
              form.globalErrors
            ))
          )
        ),
        data => {
          val credentials = Credentials(data.login, data.password)

          credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
            userService.retrieve(loginInfo).flatMap {
              case Some(user) if !user.mailVerified.contains(true) =>
                logger.warn(s"User with not verified e-mail ${data.login} tries to sign in")
                Future.successful(BadRequest(
                  Json.toJson(UserAuthenticationError("user.email.not.verified"))
                ))
              case Some(user) if user.disabled.contains(true) =>
                logger.warn(s"Disabled user ${data.login} tries to sign in")
                Future.successful(BadRequest(
                  Json.toJson(UserAuthenticationError("user.blocked"))
                ))
              case Some(user) =>
                silhouette.env.authenticatorService.create(loginInfo).map {
                  case authenticator if data.rememberMe.getOrElse(false) =>
                    authenticator.copy(
                      expirationDateTime = clock.now plus rememberMeParams._1.toMillis,
                      idleTimeout = rememberMeParams._2,
                      cookieMaxAge = rememberMeParams._3
                    )
                  case authenticator => authenticator
                }.flatMap { authenticator =>
                  silhouette.env.eventBus.publish(LoginEvent(user, request))
                  silhouette.env.authenticatorService.init(authenticator).flatMap { v =>
                    silhouette.env.authenticatorService.embed(v, Ok(""))(request)
                  }
                }
              case None =>
                logger.warn(s"No user info for credentials $loginInfo found")
                Future.successful(BadRequest(Json.toJson(UserAuthenticationError("user.not.exists"))))
            }
          }.recoverWith {
            case ex: ProviderException =>
              logger.error("Problem during User authentication occurred", ex)
              Future.successful(BadRequest(Json.toJson(UserAuthenticationError("user.provider.exception"))))
          }
        }
      )
  }

  private lazy val rememberMeParams: (FiniteDuration, Option[FiniteDuration], Option[FiniteDuration]) = {
    val cfg = configuration.get[Configuration]("silhouette.authenticator.rememberMe")
    (
      cfg.get[FiniteDuration]("authenticatorExpiry"),
      cfg.getOptional[FiniteDuration]("authenticatorIdleTimeout"),
      cfg.getOptional[FiniteDuration]("cookieMaxAge")
    )
  }

  /**
    * Create new user by submitted info
    * @return
    */
  @ApiOperation(
    value = "Sign up with new user entity",
    httpMethod = "POST"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      required = true,
      paramType = "body",
      dataTypeClass = classOf[SignUpForm.Data],
      value = "User info"
    )))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "User successfully signed up"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "DB connection error")
  ))
  def signUp = silhouette.UnsecuredAction.async { implicit request: Request[AnyContent] =>
    SignUpForm.form.bindFromRequest.fold(
      form =>
        Future.successful(BadRequest(Json.toJson(FormSubmissionError("Signup", form.errors, form.globalErrors)))),
      data => {
        val loginInfo = LoginInfo(CredentialsProvider.ID, data.email)
        userService.retrieve(loginInfo).flatMap {
          case Some(user) =>
            Future.successful(BadRequest("mail.already.registered"))
          case None =>
            val passwordInfo = passwordHasherRegistry.current.hash(data.password)

            userService.createUser(UserAccountRow(
              0L,
              Option(data.email.toLowerCase()),
              Option(passwordInfo.password),
              passwordInfo.salt,
              Option(data.email),
              Option(data.firstName),
              data.middleName,
              Option(data.lastName),
              Some(LocalDateTime.now()),
              Some(false),
              Some(false),
              Some(false),
              None,
              None
            ), Metadata.Roles.DefaultRoles).foreach{
              case (uAcc, r) =>
                val user = AppUser(uAcc, r)
                authInfoRepository.add(loginInfo, passwordInfo)
                silhouette.env.eventBus.publish(SignUpEvent(user, request))
            }
            Future.successful(Ok(""))
        }
      }
    )
  }

  def restorePassword = silhouette.UnsecuredAction.async { implicit request: Request[AnyContent] =>
    ForgotPasswordForm.form.bindFromRequest.fold(
      form =>
        Future.successful(BadRequest(Json.toJson(FormSubmissionError("restore.password", form.errors, form.globalErrors)))),
      email => {
        val loginInfo = LoginInfo(CredentialsProvider.ID, email)
        userService.retrieve(loginInfo).flatMap {
          case Some(user) =>
            logger.warn("Password restore message should be sent from here")
            Future.successful(Ok("Password restore message sent"))
          case None =>
            Future.successful(BadRequest(s"User to restore password not found by email $email"))
        }
      }
    )
  }

  def changePassword = silhouette.SecuredAction.async {
    implicit request: SecuredRequest[AppEnv, AnyContent] =>
      ChangePasswordForm.form.bindFromRequest.fold(
        form =>
          Future.successful(BadRequest(Json.toJson(FormSubmissionError("change.password", form.errors, form.globalErrors)))),
        passwordForm => {
          val (currentPassword, newPassword) = (passwordForm.currentPassword, passwordForm.newPassword)
          val credentials = Credentials(request.identity.loginInfo.providerKey, currentPassword)
          credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
            val passwordInfo = passwordHasherRegistry.current.hash(newPassword)
            authInfoRepository.update[PasswordInfo](loginInfo, passwordInfo).map { _ =>
//              Redirect(routes.ChangePasswordController.view()).flashing("success" -> Messages("password.changed"))
              Ok("")
            }
          }.recover {
            case _: ProviderException =>
//              Redirect(routes.ChangePasswordController.view()).flashing("error" -> Messages("current.password.invalid"))
            Ok("")
          }
        }
      )
  }

  def logout = silhouette.SecuredAction.async { implicit request: SecuredRequest[AppEnv, AnyContent] =>
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, Ok(Messages("user.logged.out")))
  }
}
