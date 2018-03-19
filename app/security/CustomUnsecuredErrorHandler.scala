package security

import javax.inject.Inject

import com.mohiva.play.silhouette.api.actions.UnsecuredErrorHandler
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.RequestHeader

import scala.concurrent.Future
import play.api.mvc.Results._

class CustomUnsecuredErrorHandler @Inject() (val messagesApi: MessagesApi)
  extends UnsecuredErrorHandler
    with I18nSupport {

  /**
    * Called when a user is authenticated but not authorized.
    *
    * As defined by RFC 2616, the status code of the response should be 403 Forbidden.
    *
    * @param request The request header.
    * @return The result to send to the client.
    */
  override def onNotAuthorized(implicit request: RequestHeader) = {
    Future.successful(Unauthorized(Messages("request.unauthorized")))
  }
}