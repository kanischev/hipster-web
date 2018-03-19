package codegen

import java.io.File

import com.typesafe.config.ConfigFactory
import slick.jdbc.JdbcProfile

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

//TODO: Should be extracted to sbt plugin?

object GenTables extends App {
  val slickDialectDefaultProfile = "slick.jdbc.PostgresProfile"
  val defaultJdbcDriver = classOf[org.postgresql.Driver].getCanonicalName
  val slickDatabases = Seq("default")
  val genOutputDir = new File("app")
  val tablesPackage = "models"

  val config = ConfigFactory.parseFile(new File(Seq("conf", "application.conf").mkString(File.separator))).resolve()

  def trimLast$(str: String) = {
    str.reverse.dropWhile(_ == '$').reverse
  }

  slickDatabases.foreach{dbItem =>
    val cfg = config.getConfig(s"slick.dbs.$dbItem")
    val dbCfg = config.getConfig(s"slick.dbs.$dbItem.db")
    val containerName = if (dbItem == "default") "Tables" else s"Tables${dbItem.capitalize}"

    val profile = trimLast$(if (cfg.hasPath("profile")) cfg.getString("profile") else slickDialectDefaultProfile)
    val profileInstance: JdbcProfile =
      Class.forName(profile + "$").getField("MODULE$").get(null).asInstanceOf[JdbcProfile]
    val dbFactory = profileInstance.api.Database
    val user = (if (dbCfg.hasPath("user")) Option(dbCfg.getString("user")).map(_.trim).filter(_.nonEmpty) else None).orNull
    val password = (if (dbCfg.hasPath("password")) Option(dbCfg.getString("password")).map(_.trim).filter(_.nonEmpty) else None).orNull
    val db = dbFactory.forURL(dbCfg.getString("url"), driver = if (cfg.hasPath("driver")) cfg.getString("driver") else defaultJdbcDriver,
      user = user, password = password, keepAliveConnection = true)
    try {
      val m = Await.result(db.run(profileInstance.createModel(None, ignoreInvalidDefaults = true)(ExecutionContext.global).withPinnedSession), Duration.Inf)
      val generatorInstance = new TablesCodeGen(m)
      generatorInstance.writeToFile(profile, genOutputDir.getAbsolutePath, tablesPackage, containerName)
    } finally db.close
  }
}
