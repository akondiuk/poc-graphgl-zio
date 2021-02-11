package com.kondiuk.backend

import zio._

package object configuration {
  type Configuration = Has[Configuration.Service]

  case class Config(api: ApiConfig, dbConfigMongo: DbConfigMongo)

  case class ApiConfig(endpoint: String, port: Int)

  case class DbConfig(url: String, user: String, password: String)
  case class DbConfigMongo(url: String, userCollection: String, database: String, user: String, password: String)

  def loadConfig: RIO[Configuration, Config] = RIO.accessM(_.get.load)
}
