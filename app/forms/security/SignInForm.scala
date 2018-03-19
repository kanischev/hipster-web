package forms.security

import play.api.data.Form
import play.api.data.Forms._

/**
  * The form which handles the submission of the credentials.
  */
object SignInForm {

  /**
    * A play framework form.
    */
  val form = Form(
    mapping(
      "login" -> nonEmptyText,
      "password" -> nonEmptyText,
      "rememberMe" -> optional(boolean)
    )(Data.apply)(Data.unapply)
  )

  /**
    * The form data.
    *
    * @param login The email of the user.
    * @param password The password of the user.
    * @param rememberMe Indicates if the user should stay logged in on the next visit.
    */
  case class Data(
                   login: String,
                   password: String,
                   rememberMe: Option[Boolean])
}