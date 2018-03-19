package controllers

import com.mohiva.play.silhouette.api.Silhouette
import forms.security.SignUpForm
import io.swagger.annotations.{Api, ApiOperation}
import javax.inject.Inject
import models.Tables.UserAccountRow
import play.api.data.Form
import play.api.libs.json.Format
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import security.{AppEnv, WithAnyOfRoles}
import services.UserAccountService
import utils.{Formats, Metadata}
import utils.Formats._

import scala.concurrent.ExecutionContext
import scala.reflect.ClassTag

@Api("User accounts API")
class UserAccountController @Inject()(
                                       val components: ControllerComponents,
                                       val silhouette: Silhouette[AppEnv],
                                       val crudService: UserAccountService
                                     )/*(implicit executionContext: ExecutionContext)
  extends BaseCRUDController[UserAccountRow, SignUpForm.Data, Long](components)(
    executionContext,
    ClassTag[UserAccountRow],
    Formats.userAccountFormat) {

  override def form: Form[SignUpForm.Data] = SignUpForm.form
  override def convert: SignUpForm.Data => UserAccountRow = ???

  @ApiOperation(
    value = "List all users",
    httpMethod = "GET",
    response = classOf[UserAccountRow],
    responseContainer = "List"
  )
  override def listAll: Action[AnyContent] = super.listAll

  @ApiOperation(
    value = "Create user",
    httpMethod = "POST",
    response = classOf[UserAccountRow]
  )
  override def create: Action[AnyContent] = super.create
}
*/