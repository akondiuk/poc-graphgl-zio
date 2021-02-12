package com.kondiuk.backend.configuration

import zio.{Has, Layer, Task, ZIO, ZLayer}
import pureconfig.ConfigSource

object Configuration {

  trait Service {
    val load: Task[Config]
  }

  trait Live extends Configuration.Service {

    import pureconfig.generic.auto._

    val load: Task[Config] = Task.effect(ConfigSource.default.loadOrThrow[Config])
  }

  val live: Layer[Nothing, Configuration] = ZLayer.succeed(new Live {})

  /*val test: Layer[Nothing, Has[Service]] = ZLayer.succeed(new Service {
    val load: Task[Config] = Task.effectTotal(
      Config(ApiConfig("loacalhost", 8080),
        DbConfigMongo("mongodb+srv://dev:test1234@cluster0-fsuoa.mongodb.net/test?retryWrites=true&w=majority", "gql-app", "users_", "test1234", "test1234")))
  })*/

  val apiConfig: ZIO[Has[ApiConfig], Throwable, ApiConfig] = ZIO.service
  val dbConfig: ZIO[Has[DbConfigMongo], Throwable, DbConfigMongo] = ZIO.service
}
