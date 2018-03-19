package security

import com.mohiva.play.silhouette.api.Authorization
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import play.api.mvc.Request
import utils.Metadata

import scala.concurrent.Future

/**
  * Authorizes successfully if user has any of provided roles
  * @param roles List of roles to check
  */
case class WithAnyOfRoles(roles: Seq[String]) extends Authorization[AppUser, CookieAuthenticator] {

  def isAuthorized[B](user: AppUser, authenticator: CookieAuthenticator)(
    implicit request: Request[B]) = {

    Future.successful(user.roles.contains(Metadata.Roles.Admin) || roles.exists(user.roles.contains(_)))
  }
}

/**
  * Authorizes successfully only if user has all the roles provided
  * @param roles List of roles to check
  */
case class WithAllRoles(roles: Seq[String]) extends Authorization[AppUser, CookieAuthenticator] {

  def isAuthorized[B](user: AppUser, authenticator: CookieAuthenticator)(
    implicit request: Request[B]) = {

    Future.successful(user.roles.contains(Metadata.Roles.Admin) || (roles.nonEmpty && roles.forall(user.roles.contains(_))))
  }
}

