# poc-graphgl-zio

###A simple POC repo to present GraphGL implementation with Caliban on ZIO stack.

##### Libraries and frameworks used:
* ZIO - is a library for asynchronous and concurrent programming that is based on pure functional programming
* Caliban - Functional GraphQL library for Scala
* Http4s - Typeful, functional and streaming HTTP for Scala
* ZIO-Test - Zero dependency testing library
* MongoDB - Scala Driver

##### How to run it 
* From intellij Idea open SimpleApp.scala And run it.
* From sbt console since I use the sbt-revolver plugin:
  * run reStart to start app in an incremental mode, on any class change it will automatically restart the server with changes applied
    * when run locally you can use Postman scripts which located in the postman folder or use GraphiQl tool under: http://127.0.0.1:8088/graphiql
  * when you've done just run reStop

##### How to run tests
* From intellij Idea:
  * Install ZIO for Intellij plugin - which is awailable in Intellij Idea plugins marketplace for free. It gives you an integrated test runner plus lots of flavours for refactoring.
  * open GraphQLExecutionSpec.scala And run it.
* From sbt console - just run test

##### Postman scripts located in the postman folder