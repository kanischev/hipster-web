package forms.security

import play.api.data.Forms._
import play.api.data._

/**
  * The `Change Password` form.
  */
object ChangePasswordForm {

  /**
    * A play framework form.
    */
  val form = Form(
    mapping(
      "current-password" -> nonEmptyText,
      "new-password" -> nonEmptyText,
      "new-password-repeat" -> nonEmptyText
    )(Data.apply)(Data.unapply)
  )

  case class Data(
                   currentPassword: String,
                   newPassword: String,
                   newPasswordRepeat: String
                 )
}