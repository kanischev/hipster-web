package security

import javax.inject.Inject

import com.mohiva.play.silhouette.api.actions.SecuredErrorHandler
import com.typesafe.scalalogging.LazyLogging
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.RequestHeader
import play.api.mvc.Results._

import scala.concurrent.Future

/**
  * Custom secured error handler.
  *
  * @param messagesApi The Play messages API.
  */
class CustomSecuredErrorHandler @Inject() (val messagesApi: MessagesApi)
  extends SecuredErrorHandler
    with I18nSupport
    with LazyLogging {

  /**
    * Called when a user is not authenticated.
    *
    * As defined by RFC 2616, the status code of the response should be 401 Unauthorized.
    *
    * @param request The request header.
    * @return The result to send to the client.
    */
  override def onNotAuthenticated(implicit request: RequestHeader) = {
    logger.warn(s"Not authenticated request to ${request.uri} got with headers: ${request.headers}")
    Future.successful(Forbidden(Messages("request.forbidden")))
  }

  /**
    * Called when a user is authenticated but not authorized.
    *
    * As defined by RFC 2616, the status code of the response should be 403 Forbidden.
    *
    * @param request The request header.
    * @return The result to send to the client.
    */
  override def onNotAuthorized(implicit request: RequestHeader) = {
    logger.warn(s"Not authorized request to ${request.uri} got with headers: ${request.headers}")
    Future.successful(Unauthorized(Messages("request.unauthorized")))
  }
}