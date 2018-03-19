package models

import models.Tables.UserAccountRow

case class UserCreated(userId: Long, login: String, email: String, verificationCode: String, activated: Boolean, activateUrl: Option[String])
object UserCreated {
  def apply(userAccountRow: UserAccountRow) = new UserCreated(
    userAccountRow.id,
    userAccountRow.login.getOrElse(""),
    userAccountRow.email.getOrElse(""),
    userAccountRow.verificationCode.getOrElse(""),
    !userAccountRow.disabled.getOrElse(false),
    None
  )
}
