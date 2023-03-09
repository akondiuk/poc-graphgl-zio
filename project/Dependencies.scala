import sbt._
import Keys._
import com.typesafe.sbt.SbtNativePackager.Docker
import com.typesafe.sbt.packager.Keys.{dockerBaseImage, dockerCommands, dockerExposedPorts, dockerUpdateLatest, dockerUsername, packageName}
import com.typesafe.sbt.packager.docker.ExecCmd

object Dependencies {
  //TODO upgrade to ZIO 2.0
  val ZioVersion = "1.0.0"
  //TODO upgrade to v2.0.2
  val CalibanVersion = "0.9.1"
  val Http4sVersion = "0.21.7"
  val CirceVersion = "0.13.0"
  val Specs2Version = "4.8.3"
  val LogbackVersion = "1.2.3"
  val MongoDBScalaDriverVersion = "2.9.0"

  lazy val commonDependencies = Seq(
    "dev.zio" %% "zio" % ZioVersion,
    "com.github.ghostdogpr" %% "caliban" % CalibanVersion,
    "com.github.ghostdogpr" %% "caliban-http4s" % CalibanVersion,
    "dev.zio" %% "zio-test" % ZioVersion % "test",
    "dev.zio" %% "zio-test-sbt" % ZioVersion % "test",
    "dev.zio" %% "zio-test-magnolia" % ZioVersion % "test",
    "dev.zio" %% "zio-interop-cats" % "2.1.4.0",
    "org.mongodb.scala" %% "mongo-scala-driver" % MongoDBScalaDriverVersion,
    "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
    "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
    "org.http4s" %% "http4s-circe" % Http4sVersion,
    "org.http4s" %% "http4s-dsl" % Http4sVersion,
    "io.circe" %% "circe-generic" % CirceVersion,
    "com.github.pureconfig" %% "pureconfig" % "0.14.0",
    "org.specs2" %% "specs2-core" % Specs2Version % "test",
    "ch.qos.logback" % "logback-classic" % LogbackVersion,
    "com.lihaoyi" %% "pprint" % "0.5.6",
    "io.reactivex.rxjava2" % "rxjava" % "2.2.19"
  )

  // todo Docker configuration
  lazy val dockerSettings = Seq(
    dockerExposedPorts := Seq(8088),
    dockerBaseImage := "adoptopenjdk:11.0.5_10-jdk-hotspot",
    packageName in Docker := "graphql-caliban-zio-scala",
    dockerUsername := Some("your_username")
    /*dockerCommands := {
      dockerCommands.value.flatMap {
        case ep @ ExecCmd("ENTRYPOINT", _*) =>
          Seq(
            ExecCmd("ENTRYPOINT", "/opt/docker/docker-entrypoint.sh" :: ep.args.toList: _*)
          )
        case other => Seq(other)
      }
    }*/
    /*mappings in Docker ++= {
      val scriptDir = baseDirectory.value / ".." / "scripts"
      val entrypointScript = scriptDir / "docker-entrypoint.sh"
      val entrypointScriptTargetPath = "/opt/docker/docker-entrypoint.sh"
      Seq(entrypointScript -> entrypointScriptTargetPath)
    },
    dockerUpdateLatest := true,
    publishLocal in Docker := (publishLocal in Docker).dependsOn(copyWebapp).value,
    version in Docker := git.gitHeadCommit.value.map(head => now() + "-" + head.take(8)).getOrElse("latest")*/
  )
}
