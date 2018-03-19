package dbio

import models.Tables._
import profile.api._
import slick.dbio.DBIO

class UserAccountDbio
  extends LongKeyedEntityAsyncDbio[UserAccountRow, UserAccount] {

  override def table = UserAccount

  def findByLogin(login: String): DBIO[Option[UserAccountRow]] = table.filter(_.login === login).result.headOption

  def findByLoginWithRoles(login: String): DBIO[Seq[(UserAccountRow, Option[RoleRow])]] =
    (for {
      ((user, ur), role) <- table.filter(_.login === login) joinLeft UserRole on (_.id === _.userAccountId) joinLeft Role on (_._2.map(_.roleId) === _.id)
    } yield (user, role)).result

/*

  def updateUserRoles(userId: Long, roles: Seq[String]): DBIO[Seq[String]] = {
    val action = for {
      current <- UserRole.filter(_.userAccountId === userId).result
      _ <- UserRole.filter(_.userAccountId === userId).filter(_.roleId inSet current.map(_.roleId).diff(roles)).delete
      _ <- UserRole ++= roles.diff(current.map(_.roleId)).map(UserRoleRow(userId, _))
    } yield roles

    action.transactionally
  }
*/

}
