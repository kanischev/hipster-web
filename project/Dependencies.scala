import sbt._

object Dependencies {
  // Library versions
  val SlickVersion = "3.2.1"
  val SlickCodegenVersion = "3.2.2"
  val PlaySlickVersion = "3.0.1"
  val JacksonVersion = "2.9.4"
  val SwaggerPlayVersion = "1.6.1-SNAPSHOT"
  val ScalaLoggingVersion = "3.8.0"
  val PostgresDriverVersion = "42.2.1"
  val SlickPgVersion = "0.16.0"
  val H2Version = "1.4.196"
  val HikariCpVersion = "2.7.4"
  val JodaTimeVersion = "2.9.9"
  val CatsVersion = "1.0.1"
  val SilhouetteVersion = "5.0.3"
  val ScalaGuiceVersion = "4.1.0"
  val PlayMailerVersion = "6.0.1"
  val AkkaQuartzVersion = "1.6.1-akka-2.5.x"

  // Libraries
  val dPlaySlick = "com.typesafe.play" %% "play-slick" % PlaySlickVersion
  val dPlaySlickEvolutions = "com.typesafe.play" %% "play-slick-evolutions" % PlaySlickVersion
  val dJacksonScala = "com.fasterxml.jackson.module" %% "jackson-module-scala" % JacksonVersion
  val dSwaggerPlay = "io.swagger" %% "swagger-play2" % SwaggerPlayVersion
  val dScalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % ScalaLoggingVersion
  val dPostgresDriver = "org.postgresql" % "postgresql" % PostgresDriverVersion
  val dSlickCodegen = "com.typesafe.slick" %% "slick-codegen" % SlickCodegenVersion
  val dSlick = "com.typesafe.slick" %% "slick" % SlickVersion
  val dH2 = "com.h2database" % "h2" % H2Version
  val dPlayTest = "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2"
  val dHikariCP = "com.zaxxer" % "HikariCP" % HikariCpVersion
  val dCats = "org.typelevel" %% "cats-core" % CatsVersion
  val dSilhouette = "com.mohiva" %% "play-silhouette" % SilhouetteVersion
  val dSilhouettePwdBcrypt = "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.0"
  val dSilhouettePersistance = "com.mohiva" %% "play-silhouette-persistence" % "5.0.0"
  val dSilhouetteJCA = "com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.0"

  val dScalaGuice = "net.codingwell" %% "scala-guice" % ScalaGuiceVersion
  val dPlayMailer = "com.typesafe.play" %% "play-mailer" % PlayMailerVersion
  val dPlayMailerGuice = "com.typesafe.play" %% "play-mailer-guice" % PlayMailerVersion
  val dAkkaQuartzScheduler = "com.enragedginger" %% "akka-quartz-scheduler" % AkkaQuartzVersion

  lazy val dSlickPg = Seq(
    "com.github.tminglei" %% "slick-pg",
    "com.github.tminglei" %% "slick-pg_play-json"
  ).map(_ % SlickPgVersion)

  import play.sbt.PlayImport._
  // Projects
  lazy val rootDependencies = Seq(
    guice,
    dScalaGuice,
    dJacksonScala,
    logback,
    ehcache,
    dPlaySlick,
    dPlaySlickEvolutions,
    dPlayMailer,
    dPlayMailerGuice,
    dSwaggerPlay,
    dJacksonScala,
    dScalaLogging,
    dPostgresDriver,
    dH2,
    dSlick,
    dSlickCodegen,
    dCats,
    dAkkaQuartzScheduler,
    dSilhouette,
    dSilhouetteJCA,
    dSilhouettePersistance,
    dSilhouettePwdBcrypt,
    dPlayTest % Test,
    specs2 % Test
  ) ++ dSlickPg

  lazy val pluginDependencies = Seq(
    jdbc,
    dPlaySlick,
    dPostgresDriver
  )
}