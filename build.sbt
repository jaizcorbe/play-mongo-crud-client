name := """play-mongo-crud-client"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  cache,
  ws,
  "com.typesafe.akka"	%%	"akka-actor"			%	"2.3.6",
  "org.reactivemongo"	%%	"play2-reactivemongo"	%	"0.10.5.0.akka23",
  "org.scalatestplus" 	%% 	"play" 					% 	"1.2.0" % "test"
)
