package com.github.asadaGuitar.bbs.api

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import com.github.asadaGuitar.bbs.interfaces.controllers.{ExceptionHandlers, ThreadsController, UsersController}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.asadaGuitar.bbs.interfaces.controllers.validations.RejectionHandlers
import com.typesafe.config.Config

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt
import scala.io.StdIn

object Main extends App {

  implicit val system: ActorSystem[_]       = ActorSystem(Behaviors.empty, "bbsApplication")
  implicit val ec: ExecutionContextExecutor = system.executionContext
  implicit val config: Config               = system.settings.config

  val host = config.getString("akka.http.server.host")
  val port = config.getInt("akka.http.server.port")

  val usersController  = new UsersController
  val threadController = new ThreadsController

  val router: Route =
    handleExceptions(ExceptionHandlers.defaultExceptionHandler) {
      handleRejections(RejectionHandlers.defaultRejectionHandler) {
        usersController.signinRouter ~
          usersController.signupRouter ~
          usersController.userAccountRouter ~
          threadController.threadRouter
      }
    }

  val bindingFuture =
    Http()
      .newServerAt(host, port)
      .bind(router)
      .map(_.addToCoordinatedShutdown(hardTerminationDeadline = 10.seconds))

  system.log.info(s"Server starting online http://$host:$port/")

  StdIn.readLine()
  system.terminate()
}
