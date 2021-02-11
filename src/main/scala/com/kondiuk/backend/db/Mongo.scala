package com.kondiuk.backend.db

import com.kondiuk.backend.Data.User
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.{CodecProvider, CodecRegistry}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import zio.{UIO, URIO, ZIO}

import scala.reflect.ClassTag

object Mongo {

  lazy val userCodecProvider: CodecProvider = Macros.createCodecProvider[User]()
  lazy val codecRegistry: CodecRegistry = fromRegistries(
    fromProviders(
      userCodecProvider
    ),
    DEFAULT_CODEC_REGISTRY
  )

  def mongoClient(uri: String): UIO[MongoClient] = URIO.succeed(MongoClient(uri))

  def database(dbName: String, mongoClient: MongoClient): UIO[MongoDatabase] =
    URIO.succeed(mongoClient.getDatabase(dbName).withCodecRegistry(codecRegistry))

  def collection[T](db: MongoDatabase, collectionName: String)(implicit c: ClassTag[T]): UIO[MongoCollection[T]] =
    UIO.succeed(db.getCollection(collectionName))

  def setupMongoConfiguration[T](uri: String, dbName: String, collectionName: String)
                                (implicit c: ClassTag[T]): ZIO[Any, Nothing, MongoCollection[T]] =
    for {
      mongoClient <- mongoClient(uri)
      db <- database(dbName, mongoClient)
      collection <- collection[T](db, collectionName)
    } yield collection
}
