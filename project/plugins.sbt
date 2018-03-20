// The Play plugin
val PlayVersion = "2.6.12"

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % PlayVersion)
//addSbtPlugin("com.github.tototoshi" % "sbt-slick-codegen" % "1.3.0")

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.1"