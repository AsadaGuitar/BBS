package com.github.asadaGuitar.bbs.interfaces.controllers

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler

object ExceptionHandlers {

  final def defaultExceptionHandler(implicit system: ActorSystem[_]): ExceptionHandler =
    ExceptionHandler { case e: Throwable =>
      extractUri { uri =>
        system.log.error(s"Request to $uri could not be handled normally. exception was thrown. ${e.getMessage}")
        complete(InternalServerError)
      }
    }
}
