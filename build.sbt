//import slick.codegen.SourceCodeGenerator
//import slick.{ model => m }
import sbt._
import Dependencies._


name := """hipster-web"""
version := "1.0-SNAPSHOT"

scalaVersion := "2.12.4"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    watchSources ++= (baseDirectory.value / "public/ui" ** "*").get
  )
/*
lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(slickCodegenSettings:_*)
  .settings(
    watchSources ++= (baseDirectory.value / "public/ui" ** "*").get
  ).settings(
    libraryDependencies ++= pluginDependencies,
    slickCodegenDatabaseUrl := "jdbc:postgresql://localhost/hipster",
    slickCodegenDatabaseUser := "postgres",
    slickCodegenDatabasePassword := "postgres",
    slickCodegenDriver := slick.jdbc.PostgresProfile,
    slickCodegenJdbcDriver := classOf[org.postgresql.Driver].getCanonicalName,
    slickCodegenOutputPackage := "models",
    slickCodegenExcludedTables := Seq("play_evolutions"),
    slickCodegenCodeGenerator := { (model:  m.Model) =>
      new SourceCodeGenerator(model) {
        override def code =
          "import com.github.tototoshi.slick.H2JodaSupport._\n" + "import org.joda.time.DateTime\n" + super.code
        override def Table = new Table(_) {
          override def Column = new Column(_) {
            override def rawType = model.tpe match {
              case "java.sql.Timestamp" => "DateTime" // kill j.s.Timestamp
              case _ =>
                super.rawType
            }
          }
        }
    slickCodegenOutputDir := (scalaSource in Compile).value
  )

*/

resolvers += Resolver.mavenCentral
resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)
resolvers += Resolver.sonatypeRepo("snapshots")
resolvers += Resolver.jcenterRepo

lazy val customScalacOptions = Seq(
  "-Ypartial-unification",
  //  "-Ymacro-debug-lite",
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Xfatal-warnings", // Fail the compilation if there are any warnings.
  "-Xlint", // Enable recommended additional warnings.
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
  //  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
  "-Ywarn-nullary-override", // Warn when non-nullary overrides nullary, e.g. def foo() over def foo.
  "-Ywarn-numeric-widen" // Warn when numerics are widened.
)


routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= rootDependencies

val testingOptions = "-Dconfig.file=" + Option(System.getProperty("test.config")).getOrElse("conf/development") + ".conf"

javaOptions in Test += testingOptions

publishArtifact in (Compile, packageDoc) := false
publishArtifact in packageDoc := false
sources in (Compile,doc) := Seq.empty
