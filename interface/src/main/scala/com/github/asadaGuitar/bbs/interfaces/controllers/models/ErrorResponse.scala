package com.github.asadaGuitar.bbs.interfaces.controllers.models

import com.github.asadaGuitar.bbs.interfaces.controllers.MessageProvider

final case class ErrorResponse(code: String, message: String)

final case class ErrorListResponse(errors: List[ErrorResponse])

object ErrorResponse extends MessageProvider {

  private def getErrorResponseByCode(code: String) = ErrorResponse(code, config.getString(code))

  final val signinFailure                = getErrorResponseByCode("3")

  final val userIdValidationError        = getErrorResponseByCode("")

  final val userFirstNameValidationError = getErrorResponseByCode("")

  final val userLastNameValidationError  = getErrorResponseByCode("")

  final val userPasswordValidationError  = getErrorResponseByCode("")

  final val emailAddressValidationError  = getErrorResponseByCode("")

  final val threadIdValidationError      = getErrorResponseByCode("")

  final val threadTitleValidationError   = getErrorResponseByCode("")

  final val notFoundUser                 = getErrorResponseByCode("")

  final val messageTextValidationError   = getErrorResponseByCode("")
}