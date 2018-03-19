package utils

import forms._
import models.Tables.UserAccountRow
import models.UserCreated
import play.api.libs.json._

object Formats {
  implicit val fieldErrorFormat: Format[FieldError] = Json.format[FieldError]
  implicit val formSubmissionErrorsFormat: Format[FormSubmissionError] = Json.format[FormSubmissionError]
  implicit val errorResponseFormat: Format[ErrorContainer] = Json.format[ErrorContainer]
  implicit val userAuthFormat: Format[UserAuthenticationError] = Json.format[UserAuthenticationError]
  implicit val userCreatedFormat: Format[UserCreated] = Json.format[UserCreated]

  implicit val userAccountFormat: Format[UserAccountRow] = Json.format[UserAccountRow]
}
