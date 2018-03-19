package security

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import models.Tables.UserAccountRow

case class AppUser(
                    id: Long,
                    firstName: Option[String],
                    middleName: Option[String],
                    lastName: Option[String],
                    loginInfo: LoginInfo,
                    disabled: Option[Boolean],
                    mailVerified: Option[Boolean],
                    avatarURL: Option[String],
                    roles: Seq[String]) extends Identity {

  def fullName: String = {
    Seq(firstName, middleName, lastName).flatten.mkString(" ")
  }
}

object AppUser {
  def apply(userAccountRow: UserAccountRow, roles: Seq[String] = Nil): AppUser = new AppUser(
    userAccountRow.id,
    userAccountRow.firstName,
    userAccountRow.middleName,
    userAccountRow.lastName,
    LoginInfo(CredentialsProvider.ID, userAccountRow.login.getOrElse("")),
    userAccountRow.mailVerified,
    userAccountRow.disabled,
    None,
    roles
  )
}
