package services

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import dbio.UserAccountDbio
import javax.inject.Inject
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import security.AppUser
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

/**
  * Handles actions to users.
  */
trait UserAccountService extends DBCRUDService[UserAccountRow, Long] with IdentityService[AppUser] {
  def findByLogin(login: String): Future[Option[UserAccountRow]]

  def createUser(userAccountRow: UserAccountRow, roles: Seq[String] = Nil): Future[(UserAccountRow, Seq[String])]
}

class UserAccountServiceImpl @Inject()(
                                        val dbio: UserAccountDbio,
                                        val dbConfigProvider: DatabaseConfigProvider
                                      )(implicit executionContext: ExecutionContext)
    extends UserAccountService
      with HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  override def findByLogin(login: String): Future[Option[UserAccountRow]] = db.run(
    dbio.findByLogin(login)
  )

  /**
    * @param loginInfo param with login information
    * @return optional Application User's info
    */
  override def retrieve(loginInfo: LoginInfo): Future[Option[AppUser]] = {
    db.run(
      dbio.findByLoginWithRoles(loginInfo.providerKey)
    ).map {
      case s => s.headOption.map{ case (u, _) => AppUser(u.id,
        u.firstName,
        u.middleName,
        u.lastName,
        LoginInfo(CredentialsProvider.ID, u.login.getOrElse(u.id.toString)),
        u.disabled,
        u.mailVerified,
        None,
        s.flatMap(_._2).map(_.id))
      }
      case _ => None
    }
  }

  /**
    * User's password assumed to be not crypted here
    * @param userAccount The user's entity
    * @param roles list of roles to set to User
    * @return
    */
  override def createUser(
                           userAccount: UserAccountRow,
                           roles: Seq[String]
                         ): Future[(UserAccountRow, Seq[String])] = {
    db.run{
      val action = for {
        user <- dbio.insert(userAccount)
        _ <- UserRole ++= roles.map(UserRoleRow(user.id, _))
      } yield (user, roles)

      action.transactionally
    }
  }
}
