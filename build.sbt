name := """stockz"""
organization := "com.github.ldaniels528.stockz"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
  "org.typelevel" %% "cats-effect" % "3.4.0",
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.github.ldaniels528.stockz.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.github.ldaniels528.stockz.binders._"
