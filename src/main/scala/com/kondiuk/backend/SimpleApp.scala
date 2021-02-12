package com.kondiuk.backend

import caliban.Http4sAdapter
import caliban.schema.GenericSchema
import cats.data.Kleisli
import cats.effect.Blocker
import com.kondiuk.backend.Data.User
import com.kondiuk.backend.configuration._
import com.kondiuk.backend.db.Mongo
import com.kondiuk.backend.graphql.{Api, UserService}
import org.http4s.StaticFile
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.middleware.{CORS, Logger}
import zio.blocking.Blocking
import zio.clock.Clock
import zio.console.{Console, putStrLn}
import zio.interop.catz._
import zio.{RIO, ZIO, _}

import scala.concurrent.ExecutionContext

///**
// * A simple app to test Caliban
// */
object SimpleApp extends CatsApp with GenericSchema[Console with Clock] {

  type AppTask[A] = RIO[ZEnv, A]

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program

  implicit val ec: ExecutionContext = ExecutionContext.global

  val logic = (for {
    conf <- loadConfig
    userCollection <- Mongo.setupMongoConfiguration[User](
      conf.dbConfigMongo.url,
      conf.dbConfigMongo.database,
      conf.dbConfigMongo.userCollection
    )
    _ <- UserService
      .make(userCollection)
      .memoize
      .use(
        layer =>
          for {
            blocker <- ZIO.access[Blocking](_.get.blockingExecutor.asEC).map(Blocker.liftExecutionContext)
            interpreter <- Api.api.interpreter.map(_.provideCustomLayer(layer))
            _ <- BlazeServerBuilder[AppTask](ExecutionContext.global)
              .bindHttp(8088, "localhost")
              .withHttpApp(
                Logger.httpApp(logHeaders = true, logBody = true)(httpApp =
                  Router[AppTask](
                    "/api/graphql" -> CORS(Http4sAdapter.makeHttpService(interpreter)),
                    "/graphiql" -> Kleisli.liftF(StaticFile.fromResource("/graphiql.html", blocker, None))
                  ).orNotFound
                ))
              .resource
              .toManaged
              .useForever
          } yield ExitCode(0)
      )
  } yield ExitCode(0))
    .catchAll(err => putStrLn(err.toString).as(ExitCode(1)))

  //  val liveEnvironments: Layer[Nothing, zio.ZEnv] = zio.ZEnv.live ++ Configuration.live
  val liveEnvironments = zio.ZEnv.live ++ Configuration.live
  /*++ UserService.mocked*/
  private val program = logic.provideLayer(liveEnvironments)
}
