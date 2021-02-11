package com.kondiuk.backend

object Data {

  type UserId = Int

  case class User(
                   firstName: String,
                   lastName: String,
                   dateOfBirth: String,
                   occupation: String,
                   city: String,
                   bio: String
                 )

  case class UserNotFound(name: String) extends Throwable

}
