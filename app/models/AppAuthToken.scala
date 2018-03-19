package models

import java.time.LocalDateTime

import models.Tables.UserAuthTokenRow

case class AppAuthToken(id: String,
                        userId: Long,
                        host: String,
                        lastRequestPath: String,
                        creationTime: LocalDateTime,
                        lastRequestTime: LocalDateTime,
                        expiryTime: LocalDateTime)

object AppAuthToken {
  def apply(userAuthTokenRow: UserAuthTokenRow): AppAuthToken = new AppAuthToken(
    userAuthTokenRow.id,
    userAuthTokenRow.userId.get,
    "",
    "",
    userAuthTokenRow.creationTime,
    userAuthTokenRow.lastRequestTime.get,
    userAuthTokenRow.expiryTime
  )
}
