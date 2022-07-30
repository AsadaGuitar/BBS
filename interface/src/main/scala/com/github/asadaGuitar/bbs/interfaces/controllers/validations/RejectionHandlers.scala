package com.github.asadaGuitar.bbs.interfaces.controllers.validations

import akka.http.javadsl.server.CustomRejection
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.RejectionHandler
import cats.data.NonEmptyList
import com.github.asadaGuitar.bbs.interfaces.controllers.Marshaller
import com.github.asadaGuitar.bbs.interfaces.controllers.models.{ErrorListResponse, ErrorResponse}

object RejectionHandlers extends Marshaller {
  final val defaultRejectionHandler: RejectionHandler = RejectionHandler
    .newBuilder()
    .handle {
      case ValidationErrorRejection(errors) =>
        complete(
          StatusCodes.BadRequest,
          ErrorListResponse(
            errors.toList
          )
        )
    }
    .result()
}

final case class ValidationErrorRejection(errors: NonEmptyList[ErrorResponse]) extends CustomRejection