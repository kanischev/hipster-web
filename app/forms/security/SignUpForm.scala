package forms.security

import play.api.data.Form
import play.api.data.Forms._

object SignUpForm {
  /**
    * A play framework form.
    */
  val form = Form(
    mapping(
      "email" -> email,
      "firstName" -> nonEmptyText,
      "middleName" -> optional(text),
      "lastName" -> nonEmptyText,
      "password" -> tuple(
        "main" -> nonEmptyText,
        "confirm" -> nonEmptyText
      ).verifying(
        "Passwords don't match", password => password._1 == password._2
      ).transform[String](
        password => password._1,
        password => ("", "")
      )
    )(Data.apply)(Data.unapply)
  )

  /**
    * The form data.
    *
    * @param email The email of the user.
    * @param firstName The first name of a user.
    * @param middleName Optional middle name of a user.
    * @param lastName The last name of a user.
    * @param password The password of the user.
    */
  case class Data(
                   email: String,
                   firstName: String,
                   middleName: Option[String],
                   lastName: String,
                   password: String
                 )
}