package com.kondiuk.backend.graphql

import caliban.GraphQL.graphQL
import caliban.schema.Annotations.GQLDescription
import caliban.schema.GenericSchema
import caliban.wrappers.ApolloTracing.apolloTracing
import caliban.wrappers.Wrappers._
import caliban.{GraphQL, RootResolver}
import com.kondiuk.backend.Data._
import com.kondiuk.backend.graphql.UserService.UserService
import zio.RIO
import zio.clock.Clock
import zio.console.Console
import zio.duration._

object Api extends GenericSchema[UserService] {

  final case class FindUserArgs(firstName: String)

  final case class EditUserArgs(user: User)

  final case class AddUserArgs(user: User)

  case class Queries(
                      @GQLDescription("Returns a user")
                      findUser: FindUserArgs => RIO[UserService, User],
                      @GQLDescription("Returns all user")
                      findUsers: () => RIO[UserService, Seq[User]]
                    )

  case class Mutations(
                        @GQLDescription("Edit selected users")
                        editUser: EditUserArgs => RIO[UserService, Long],
                        @GQLDescription("Add a user")
                        addUser: AddUserArgs => RIO[UserService, Unit]
                      )

  case class UserView(firstName: String, user: User)

  implicit val findUserArgs: Api.Typeclass[FindUserArgs] = gen[FindUserArgs]
  implicit val editUserArgs: Api.Typeclass[EditUserArgs] = gen[EditUserArgs]
  implicit val addUserArgs: Api.Typeclass[AddUserArgs] = gen[AddUserArgs]

  // Api
  val api: GraphQL[Console with Clock with UserService] =
    graphQL(
      RootResolver(
        Queries(
          args => UserService.findUser(args.firstName),
          () => UserService.findUsers()
        ),
        Mutations(
          args => UserService.editUser(args.user),
          args => UserService.addUser(args.user)
        )
      )
    ) @@
      maxFields(200) @@ // query analyzer that limit query fields
      maxDepth(30) @@ // query analyzer that limit query depth
      timeout(3 second) @@ // wrapper that fails slow queries
      printSlowQueries(500 millis) @@ // wrapper that logs slow queries
      printErrors @@ // wrapper that logs errors
      apolloTracing // wrapper for https://github.com/apollographql/apollo-tracing
}
