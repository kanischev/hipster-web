package security

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.{PasswordHasherRegistry, PasswordInfo}
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import com.typesafe.scalalogging.LazyLogging
import javax.inject.Inject
import services.UserAccountService

import scala.concurrent.{ExecutionContext, Future}

class AppAuthInfoDAO @Inject()(
                                val userAccountService: UserAccountService,
                                val passwordHasherRegistry: PasswordHasherRegistry
                              )(implicit executionContext: ExecutionContext)
  extends DelegableAuthInfoDAO[PasswordInfo]
    with LazyLogging {

  override def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    userAccountService.findByLogin(loginInfo.providerKey).map{
      case Some(uAcc) if !uAcc.disabled.contains(true) => Some(PasswordInfo(passwordHasherRegistry.current.id, uAcc.passwordHash.get, uAcc.passwordSalt))
      case _ => None
    }
  }

  override def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    userAccountService.findByLogin(loginInfo.providerKey).map {
      case Some(uAcc) if !uAcc.disabled.contains(true) =>
        userAccountService.update(uAcc.copy(passwordHash = Option(authInfo.password), passwordSalt = authInfo.salt))
        authInfo
      case _ =>
        logger.error(s"No user account found for login = ${loginInfo.providerKey} and provider = ${loginInfo.providerID}. Password can not br updated")
        throw new RuntimeException(s"The user for ${loginInfo.providerKey} not found")
    }
  }

  override def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    add(loginInfo, authInfo)
  }

  override def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = throw new UnsupportedOperationException("DB saving should be done with user's sign up")

  override def remove(loginInfo: LoginInfo): Future[Unit] = throw new UnsupportedOperationException("Password info should not be removed from DB repo")
}