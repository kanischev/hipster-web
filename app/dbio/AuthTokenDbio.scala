package dbio

import java.time.LocalDateTime

import models.Tables._
import profile.api._

class AuthTokenDbio
  extends StringKeyedEntityAsyncDbio[UserAuthTokenRow, UserAuthToken] {

  override def table = UserAuthToken

  def findExpired(dateTime: LocalDateTime): DBIO[Seq[UserAuthTokenRow]] = {
    val currentTime = LocalDateTime.now()
    (for (e <- table.filter(_.expiryTime <= currentTime)) yield e).result
  }
}


