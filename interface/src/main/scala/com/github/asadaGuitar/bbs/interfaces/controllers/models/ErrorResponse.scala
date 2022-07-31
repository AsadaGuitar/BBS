package com.github.asadaGuitar.bbs.interfaces.controllers.models

import com.github.asadaGuitar.bbs.interfaces.controllers.MessageProvider

final case class ErrorResponse(code: String, message: String)

final case class ErrorListResponse(errors: List[ErrorResponse])

object ErrorResponse extends MessageProvider {

  private def getErrorResponseByCode(code: String) = ErrorResponse(code, config.getString(code))

  final val signinFailure                = getErrorResponseByCode("1111")

  final val userIdValidationError        = getErrorResponseByCode("1111")

  final val userFirstNameValidationError = getErrorResponseByCode("1111")

  final val userLastNameValidationError  = getErrorResponseByCode("1111")

  final val userPasswordValidationError  = getErrorResponseByCode("1111")

  final val emailAddressValidationError  = getErrorResponseByCode("1111")

  final val threadIdValidationError      = getErrorResponseByCode("1111")

  final val threadTitleValidationError   = getErrorResponseByCode("1111")

  final val notFoundUser                 = getErrorResponseByCode("1111")

  final val messageTextValidationError   = getErrorResponseByCode("1111")
}