package forms

import play.api.data.FormError

case class ErrorContainer(error: String)
case class UserAuthenticationError(error: String)

case class FieldError(key: String, messages: Seq[String])

case class FormSubmissionError(
                                error: String,
                                fieldErrors: Seq[FieldError]
                              )
object FormSubmissionError {
  def apply(errorMessage: String,
            fieldErrors: Seq[FormError] = Nil,
            globalErrors: Seq[FormError] = Nil) =
    new FormSubmissionError(
      errorMessage,
      fieldErrors.map(e => FieldError(e.key, e.messages))
    )
}