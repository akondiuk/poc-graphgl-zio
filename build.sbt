import Dependencies.dockerSettings

lazy val root = (project in file("."))
  .settings(
    organization := "com.kondiuk",
    name := "graphql-caliban-zio-scala",
    version := "0.0.1-SNAPSHOT",
    //TODO upgrade to Scala 3
    scalaVersion := "2.13.1",
    mainClass in Compile := Some("com.kondiuk.backend.SimpleApp"),
  libraryDependencies ++= Dependencies.commonDependencies,
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.0"),
    testFrameworks ++= Seq(new TestFramework("zio.test.sbt.ZTestFramework")),
    dockerSettings
)
  .enablePlugins(DockerPlugin)
  .enablePlugins(JavaServerAppPackaging)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings",
  "-Ymacro-annotations"
)


