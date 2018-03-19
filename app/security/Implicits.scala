package security

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider

object Implicits {
  implicit def key2loginInfo(key: String): LoginInfo = LoginInfo(CredentialsProvider.ID, key)
  implicit def loginInfo2key(loginInfo: LoginInfo): String = loginInfo.providerKey
  implicit def passwordInfo2pwd(passwordInfo: PasswordInfo): String = passwordInfo.password
}
