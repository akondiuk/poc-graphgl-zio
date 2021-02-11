import caliban.Macros.gqldoc
import com.kondiuk.backend.graphql.Api.api
import com.kondiuk.backend.graphql.UserService
import zio.duration._
import zio.test.Assertion.equalTo
import zio.test.TestAspect.{jvmOnly, nonFlaky}
import zio.test.{DefaultRunnableSpec, TestAspect, assertM, suite, testM}

object GraphQLExecutionSpec extends DefaultRunnableSpec {

  def spec = suite("GraphQLExecutionSpec")(
    suite("unit tests")(
      testM("Test findUser query") {
        val interpreter = api.interpreter

        val query = gqldoc(
          """{
                       findUser(firstName: "Peter") {
                                firstName,
                                lastName,
                                dateOfBirth,
                                occupation,
                                city,
                                bio
                                }
                              }""")

        assertM(interpreter.flatMap(_.execute(query)).map(_.data.toString))(
          equalTo(
            """{"findUser":{"firstName":"Peter","lastName":"Wilson","dateOfBirth":"12/09/1988","occupation":"Programmer","city":"Austin","bio":"Brave one"}}""")
        ).provideCustomLayer(UserService.mockedZLayer)
      },
      testM("Test findUser query limiting with only firstname returning") {
        val interpreter = api.interpreter

        val query = gqldoc(
          """{
                       findUser(firstName: "Peter") {
                                firstName
                                }
                              }""")

        assertM(interpreter.flatMap(_.execute(query)).map(_.data.toString))(
          equalTo(
            """{"findUser":{"firstName":"Peter"}}""")
        ).provideCustomLayer(UserService.mockedZLayer)
      }

    )
  )

}
