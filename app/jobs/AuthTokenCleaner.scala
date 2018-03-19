package jobs

import javax.inject.Inject

import akka.actor.Actor
import com.mohiva.play.silhouette.api.util.Clock
import com.typesafe.scalalogging.LazyLogging
import jobs.AuthTokenCleaner.Clean
import services.AuthTokenService

import scala.concurrent.ExecutionContext

class AuthTokenCleaner @Inject() (
                                   service: AuthTokenService,
                                   clock: Clock)(implicit ec: ExecutionContext)
  extends Actor with LazyLogging {

  /**
    * Process the received messages.
    */
  def receive: Receive = {
    case Clean =>
      val start = clock.now.getMillis
      val msg = new StringBuffer("\n")
      msg.append("=================================\n")
      msg.append("Start to cleanup auth tokens\n")
      msg.append("=================================\n")
      service.clean.map { deleted =>
        val seconds = (clock.now.getMillis - start) / 1000
        msg.append("Total of %s auth tokens(s) were deleted in %s seconds".format(deleted.length, seconds)).append("\n")
        msg.append("=================================\n")

        msg.append("=================================\n")
        logger.info(msg.toString)
      }.recover {
        case e =>
          msg.append("Couldn't cleanup auth tokens because of unexpected error\n")
          msg.append("=================================\n")
          logger.error(msg.toString, e)
      }
  }
}

/**
  * The companion object.
  */
object AuthTokenCleaner {
  case object Clean
}