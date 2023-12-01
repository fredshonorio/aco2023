ThisBuild / scalaVersion := "2.13.12"

lazy val aoc2023 = (project in file("."))
  .settings(
    version              := "0.0",
    Compile / run / fork := true,
    libraryDependencies ++= Seq(
      "co.fs2"        %% "fs2-io"      % "3.9.3",
      "org.typelevel" %% "cats-effect" % "3.5.2",
    )
  )
