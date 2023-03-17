ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.1"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.4.8"

lazy val root = (project in file("."))
  .settings(
    name := "pitupitu"
  )
