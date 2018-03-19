package services

import java.time.LocalDateTime
import java.util.UUID

import com.typesafe.scalalogging.LazyLogging
import dbio.AuthTokenDbio
import javax.inject.Inject
import models.AppAuthToken
import models.Tables.UserAuthTokenRow
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Handles actions to auth tokens.
  */
trait AuthTokenService {

  /**
    * Creates a new auth token and saves it in the backing store.
    *
    * @param userId The user ID for which the token should be created.
    * @param expiry The duration a token expires.
    * @return The saved auth token.
    */
  def create(userId: Long, expiry: FiniteDuration = 5 minutes): Future[AppAuthToken]

  /**
    * Validates a token ID.
    *
    * @param id The token ID to validate.
    * @return The token if it's valid, None otherwise.
    */
  def validate(id: String): Future[Option[AppAuthToken]]

  /**
    * Validates a token ID and updates its expiry time
    *
    * @param id The token ID to validate.
    * @return The token if it's valid, None otherwise.
    */
  def validateAndUpdate(id: String, requestPath: String, expiry: FiniteDuration = 5 minutes): Future[Option[AppAuthToken]]

  /**
    * Cleans expired tokens.
    *
    * @return The list of deleted tokens.
    */
  def clean: Future[Seq[AppAuthToken]]
}

class AuthTokenServiceImpl @Inject()(
                                      val dbConfigProvider: DatabaseConfigProvider,
                                      authTokenDbio: AuthTokenDbio
                                    )(implicit executionContext: ExecutionContext)
  extends AuthTokenService
    with HasDatabaseConfigProvider[JdbcProfile]
    with LazyLogging {

  /**
    * Creates a new auth token and saves it in the backing store.
    *
    * @param userId The user ID for which the token should be created.
    * @param expiry The duration a token expires.
    * @return The saved auth token.
    */
  override def create(userId: Long,
                      expiry: FiniteDuration): Future[AppAuthToken] = db.run {
    authTokenDbio.insert(UserAuthTokenRow(
      UUID.randomUUID().toString,
      Some(userId),
      None,
      None,
      LocalDateTime.now(),
      Some(LocalDateTime.now()),
      LocalDateTime.now() plusNanos expiry.toNanos
    ))
  }.map(AppAuthToken(_))

  /**
    * Validates a token ID.
    *
    * @param id The token ID to validate.
    * @return The token if it's valid, None otherwise.
    */
  override def validate(id: String): Future[Option[AppAuthToken]] = {
    val now = LocalDateTime.now()
    db.run {
      authTokenDbio.lookup(id)
    } map {
      case Some(token) if token.expiryTime isAfter now => Some(AppAuthToken(token))
      case Some(_) =>
        logger.warn(s"Requested for validation token $id is already axpired")
        None
      case _ => None
    }
  }

  /**
    * Validates a token ID and updates its expiry time
    *
    * @param id The token ID to validate.
    * @return The token if it's valid, None otherwise.
    */
  override def validateAndUpdate(id: String, requestPath: String, expiry: FiniteDuration): Future[Option[AppAuthToken]] = Future.successful(None)

  /**
    * Cleans expired tokens.
    *
    * @return The list of deleted tokens.
    */
  override def clean: Future[Seq[AppAuthToken]] = Future.successful(Nil)
}