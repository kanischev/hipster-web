package forms.security

import play.api.data.Forms._
import play.api.data._

object ForgotPasswordForm {

  /**
    * A play framework form.
    */
  val form = Form(
    "email" -> email
  )
}