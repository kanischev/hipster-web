package controllers

import com.typesafe.scalalogging.LazyLogging
import forms.{ErrorContainer, FormSubmissionError}
import models.TypedKeyedEntity
import play.api.data.Form
import play.api.libs.json.{Format, Json}
import play.api.mvc._
import services.DBCRUDService
import utils.Formats._

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

case class DeletedDescription(deleted: Int)

abstract class BaseCRUDController[E <: TypedKeyedEntity[K], F, K] (components: ControllerComponents)/*(
  implicit executionContext: ExecutionContext,
  evidence: ClassTag[E],
  format: Format[E]
) extends AbstractController(components)
  with LazyLogging {

  implicit val deletedFormat: Format[DeletedDescription] = Json.format[DeletedDescription]

  def crudService: DBCRUDService[E, K]
  def entityName: String = evidence.runtimeClass.getName
  def form: Form[F]
  def convert: (F) => E

  def create: Action[AnyContent] = Action.async { request =>
    form.bindFromRequest().fold(
      form => Future.successful(
        BadRequest(
          Json.toJson(FormSubmissionError(
            s"entity.${entityName.toLowerCase}.create.error",
            form.errors,
            form.globalErrors
          ))
        )
      ),
      data => {
        crudService.insert(convert.apply(data)).map { e =>
          Ok(Json.toJson(e))
        }
      }
    ).recoverWith {
      case ex: Exception =>
        logger.error(s"Error occurred during entity \'${entityName.toLowerCase}\' creation.", ex)
        Future.successful(BadRequest(Json.toJson(new FormSubmissionError(
          s"entity.${entityName.toLowerCase}.create.error",
          Nil,
          Nil
        ))))
    }
  }

  def update: Action[AnyContent] = Action.async { request =>
    form.bindFromRequest().fold(
      form => Future.successful(
        BadRequest(
          Json.toJson(FormSubmissionError(
            s"entity.${entityName.toLowerCase}.update.error",
            form.errors,
            form.globalErrors
          ))
        )
      ),
      data => {
        val entity = convert.apply(data)
        crudService.update(entity).map { _ =>
          Ok(Json.toJson(entity))
        }
      }
    ).recoverWith {
      case ex: Exception =>
        logger.error(s"Error occurred during entity \'${entityName.toLowerCase}\' update.", ex)
        Future.successful(BadRequest(Json.toJson(new FormSubmissionError(
          s"entity.${entityName.toLowerCase}.update.error",
          Nil,
          Nil
        ))))
    }
  }

  def delete(id: K) = Action.async {
   crudService.delete(id).map(i => {
     Ok(Json.toJson(DeletedDescription(i)))
   })
  }

  def lookup(id: K) = Action.async {
    crudService.lookup(id).map{
      case Some(e) => Ok(Json.toJson(e))
      case None => BadRequest(Json.toJson(ErrorContainer(s"entity.${entityName.toLowerCase}.not.found")))
    }
  }

  def listAll = Action.async {
    crudService.listAll.map {e =>
      Ok(Json.toJson(e))
    }
  }
}
*/