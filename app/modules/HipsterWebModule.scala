package modules

import com.google.inject.Provides
import com.mohiva.play.silhouette.api.actions.{SecuredErrorHandler, UnsecuredErrorHandler}
import com.mohiva.play.silhouette.api.crypto.{Crypter, CrypterAuthenticatorEncoder, Signer}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.AuthenticatorService
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.api.{Environment, EventBus, Silhouette, SilhouetteProvider}
import com.mohiva.play.silhouette.crypto.{JcaCrypter, JcaCrypterSettings, JcaSigner, JcaSignerSettings}
import com.mohiva.play.silhouette.impl.authenticators.{CookieAuthenticator, CookieAuthenticatorService, CookieAuthenticatorSettings}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.providers.state.{CsrfStateItemHandler, CsrfStateSettings}
import com.mohiva.play.silhouette.impl.util.{DefaultFingerprintGenerator, PlayCacheLayer, SecureRandomIDGenerator}
import com.mohiva.play.silhouette.password.{BCryptPasswordHasher, BCryptSha256PasswordHasher}
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.persistence.repositories.DelegableAuthInfoRepository
import javax.inject.Named
import net.codingwell.scalaguice.ScalaModule
import play.api
import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.CookieHeaderEncoding
import security.{AppAuthInfoDAO, AppEnv, CustomSecuredErrorHandler, CustomUnsecuredErrorHandler}
import services.{AuthTokenService, AuthTokenServiceImpl, UserAccountService, UserAccountServiceImpl}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration

class HipsterWebModule
  extends ScalaModule {

  override def configure(): Unit = {
    bindServices()
    // Silhouette bindings
    bind[Silhouette[AppEnv]].to[SilhouetteProvider[AppEnv]]
    bind[UnsecuredErrorHandler].to[CustomUnsecuredErrorHandler]
    bind[SecuredErrorHandler].to[CustomSecuredErrorHandler]
    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
    bind[CacheLayer].to[PlayCacheLayer]
    bind[FingerprintGenerator].toInstance(new DefaultFingerprintGenerator(false))
    bind[EventBus].toInstance(EventBus())
    bind[Clock].toInstance(Clock())
    bind[DelegableAuthInfoDAO[PasswordInfo]].to(classOf[AppAuthInfoDAO])
  }

  def bindServices(): Unit = {
    bind[AuthTokenService].to[AuthTokenServiceImpl].asEagerSingleton()
    bind[UserAccountService].to[UserAccountServiceImpl].asEagerSingleton()
  }

  /**
    * Provides the Silhouette environment.
    *
    * @param userService The user service implementation.
    * @param authenticatorService The authentication service implementation.
    * @param eventBus The event bus instance.
    * @return The Silhouette environment.
    */
  @Provides
  def provideEnvironment(
                          userService: UserAccountService,
                          authenticatorService: AuthenticatorService[CookieAuthenticator],
                          eventBus: EventBus): Environment[AppEnv] = {

    Environment[AppEnv](
      userService,
      authenticatorService,
      Seq(),
      eventBus
    )
  }


  /**
    * Provides the HTTP layer implementation.
    *
    * @param client Play's WS client.
    * @return The HTTP layer implementation.
    */
  @Provides
  def provideHTTPLayer(client: WSClient): HTTPLayer = new PlayHTTPLayer(client)



  /**
    * Provides the signer for the CSRF state item handler.
    *
    * @param configuration The Play configuration.
    * @return The signer for the CSRF state item handler.
    */
  @Provides @Named("csrf-state-item-signer")
  def provideCSRFStateItemSigner(configuration: api.Configuration): Signer = {
    new JcaSigner(JcaSignerSettings(configuration.get[String]("silhouette.csrfStateItemHandler.signer.key")))
  }

  /**
    * Provides the signer for the authenticator.
    *
    * @param configuration The Play configuration.
    * @return The signer for the authenticator.
    */
  @Provides @Named("authenticator-signer")
  def provideAuthenticatorSigner(configuration: Configuration): Signer = {
    new JcaSigner(JcaSignerSettings(configuration.get[String]("silhouette.authenticator.signer.key")))
  }

  /**
    * Provides the crypter for the authenticator.
    *
    * @param configuration The Play configuration.
    * @return The crypter for the authenticator.
    */
  @Provides @Named("authenticator-crypter")
  def provideAuthenticatorCrypter(configuration: Configuration): Crypter = {
    new JcaCrypter(JcaCrypterSettings(configuration.get[String]("silhouette.authenticator.crypter.key")))
  }

  /**
    * Provides the auth info repository.
    *
    * @param passwordInfoDAO The implementation of the delegable password auth info DAO.
    * @return The auth info repository instance.
    */
  @Provides
  def provideAuthInfoRepository(passwordInfoDAO: DelegableAuthInfoDAO[PasswordInfo]): AuthInfoRepository = {
    new DelegableAuthInfoRepository(passwordInfoDAO)
  }
  /**
    * Provides the authenticator service.
    *
    * @param signer The signer implementation.
    * @param crypter The crypter implementation.
    * @param cookieHeaderEncoding Logic for encoding and decoding `Cookie` and `Set-Cookie` headers.
    * @param fingerprintGenerator The fingerprint generator implementation.
    * @param idGenerator The ID generator implementation.
    * @param configuration The Play configuration.
    * @param clock The clock instance.
    * @return The authenticator service.
    */
  @Provides
  def provideAuthenticatorService(
                                   @Named("authenticator-signer") signer: Signer,
                                   @Named("authenticator-crypter") crypter: Crypter,
                                   cookieHeaderEncoding: CookieHeaderEncoding,
                                   fingerprintGenerator: FingerprintGenerator,
                                   idGenerator: IDGenerator,
                                   configuration: Configuration,
                                   clock: Clock): AuthenticatorService[CookieAuthenticator] = {

    val authenticatorEncoder = new CrypterAuthenticatorEncoder(crypter)

    new CookieAuthenticatorService(CookieAuthenticatorSettings(
      configuration.get[String]("silhouette.authenticator.cookieName"),
      configuration.get[String]("silhouette.authenticator.cookiePath"),
      configuration.getOptional[String]("silhouette.authenticator.cookiePath"),
      configuration.get[Boolean]("silhouette.authenticator.secureCookie"),
      configuration.get[Boolean]("silhouette.authenticator.httpOnlyCookie"),
      configuration.get[Boolean]("silhouette.authenticator.useFingerprinting"),
      configuration.getOptional[FiniteDuration]("silhouette.authenticator.cookieMaxAge"),
      configuration.getOptional[FiniteDuration]("silhouette.authenticator.authenticatorIdleTimeout"),
      configuration.get[FiniteDuration]("silhouette.authenticator.authenticatorExpiry")
    ), None, signer, cookieHeaderEncoding, authenticatorEncoder, fingerprintGenerator, idGenerator, clock)
  }


  /**
    * Provides the CSRF state item handler.
    *
    * @param idGenerator The ID generator implementation.
    * @param signer The signer implementation.
    * @param configuration The Play configuration.
    * @return The CSRF state item implementation.
    */
  @Provides
  def provideCsrfStateItemHandler(
                                   idGenerator: IDGenerator,
                                   @Named("csrf-state-item-signer") signer: Signer,
                                   configuration: Configuration): CsrfStateItemHandler = {
    new CsrfStateItemHandler(CsrfStateSettings(
      configuration.get[String]("silhouette.csrfStateItemHandler.cookieName"),
      configuration.get[String]("silhouette.csrfStateItemHandler.cookiePath"),
      configuration.getOptional[String]("silhouette.csrfStateItemHandler.cookiePath"),
      configuration.get[Boolean]("silhouette.csrfStateItemHandler.secureCookie"),
      configuration.get[Boolean]("silhouette.csrfStateItemHandler.httpOnlyCookie"),
      configuration.get[FiniteDuration]("silhouette.csrfStateItemHandler.expirationTime")

    ), idGenerator, signer)
  }


  /**
    * Provides the password hasher registry.
    *
    * @return The password hasher registry.
    */
  @Provides
  def providePasswordHasherRegistry(): PasswordHasherRegistry = {
    PasswordHasherRegistry(new BCryptSha256PasswordHasher(), Seq(new BCryptPasswordHasher()))
  }

  /**
    * Provides the credentials provider.
    *
    * @param authInfoRepository The auth info repository implementation.
    * @param passwordHasherRegistry The password hasher registry.
    * @return The credentials provider.
    */
  @Provides
  def provideCredentialsProvider(
                                  authInfoRepository: AuthInfoRepository,
                                  passwordHasherRegistry: PasswordHasherRegistry): CredentialsProvider = {

    new CredentialsProvider(authInfoRepository, passwordHasherRegistry)
  }

}
