import sbt._

organization := "com.github.gvolpe"

name := """play-mongo-crud-client"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.2"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "org.reactivemongo"	%%	"play2-reactivemongo"	%	"0.10.5.0.akka23",
  "org.scalatestplus" 	%% 	"play" 			% 	"1.2.0" % "test"
)

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "content/repositories/releases")
}

autoScalaLibrary := false

publishMavenStyle := true
