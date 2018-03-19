package jobs

import javax.inject.{Inject, Named}

import akka.actor.{ActorRef, ActorSystem}
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

/**
  * Schedules the jobs.
  */
class Scheduler @Inject() (
                            system: ActorSystem,
                            @Named("auth-token-cleaner") authTokenCleaner: ActorRef) {

  QuartzSchedulerExtension(system).schedule("AuthTokenCleaner", authTokenCleaner, AuthTokenCleaner.Clean)

  authTokenCleaner ! AuthTokenCleaner.Clean
}