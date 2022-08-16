package com.github.asadaGuitar.bbs.api

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ ActorSystem, Behavior }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.github.asadaGuitar.bbs.interfaces.controllers.validations.RejectionHandlers
import com.github.asadaGuitar.bbs.interfaces.controllers.{ ExceptionHandlers, ThreadsController, UsersController }
import com.typesafe.config.Config

import scala.concurrent.{ ExecutionContext, Future }
import scala.concurrent.duration.DurationInt
import scala.util.{ Failure, Success }

object Main extends App {

  sealed trait Message
  private case class StartSucceeded(msg: String) extends Message
  private case class StartFailed(msg: String) extends Message

  def apply(): Behavior[Message] = Behaviors.setup[Message] { ctx =>
    implicit val system: ActorSystem[_] = ctx.system
    implicit val ec: ExecutionContext   = ctx.executionContext
    implicit val config: Config         = ctx.system.settings.config

    val host: String = config.getString("akka.http.server.host")
    val port: Int    = config.getInt("akka.http.server.port")

    val usersController  = new UsersController
    val threadController = new ThreadsController

    val router: Route = {
      handleExceptions(ExceptionHandlers.defaultExceptionHandler) {
        handleRejections(RejectionHandlers.defaultRejectionHandler) {
          usersController.signinRouter ~
          usersController.signupRouter ~
          usersController.userAccountRouter ~
          threadController.threadRouter
        }
      }
    }

    val bindingFuture: Future[Http.ServerBinding] =
      Http()
        .newServerAt(host, port)
        .bind(router)
        .map(_.addToCoordinatedShutdown(hardTerminationDeadline = 10.seconds))

    ctx.pipeToSelf(bindingFuture) {
      case Success(serverBinding) =>
        val address = serverBinding.localAddress
        StartSucceeded(s"server bound to http://${address.getHostString}:${address.getPort}")
      case Failure(ex) =>
        StartFailed(s"Failed to bind endpoint, terminating system: $ex")
    }

    Behaviors.receiveMessage {
      case StartSucceeded(msg) =>
        ctx.log.info(msg)
        Behaviors.same
      case StartFailed(msg) =>
        ctx.log.info(msg)
        Behaviors.stopped
    }
  }

  ActorSystem(Main(), "bbsApplication")

}
