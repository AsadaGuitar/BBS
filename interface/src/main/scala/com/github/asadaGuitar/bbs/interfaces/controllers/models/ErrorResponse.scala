package com.github.asadaGuitar.bbs.interfaces.controllers.models

import com.github.asadaGuitar.bbs.interfaces.controllers.MessageProvider

final case class ErrorResponse(code: String, message: String)

final case class ErrorListResponse(errors: List[ErrorResponse])

object ErrorResponse extends MessageProvider {

  private def getErrorResponseByCode(code: String) = ErrorResponse(code, config.getString(code))

  final val userIdValidationError: ErrorResponse = getErrorResponseByCode("010101")

  final val userFirstNameValidationError: ErrorResponse = getErrorResponseByCode("010102")

  final val userLastNameValidationError: ErrorResponse = getErrorResponseByCode("010103")

  final val userPasswordValidationError: ErrorResponse = getErrorResponseByCode("010104")

  final val notFoundUser: ErrorResponse = getErrorResponseByCode("010105")

  final val emailAddressValidationError: ErrorResponse = getErrorResponseByCode("010201")

  final val threadIdValidationError: ErrorResponse = getErrorResponseByCode("010301")

  final val threadTitleValidationError: ErrorResponse = getErrorResponseByCode("010302")

  final val messageIdValidationError: ErrorResponse = getErrorResponseByCode("010401")

  final val messageTextValidationError: ErrorResponse = getErrorResponseByCode("010402")

  final val signinFailure: ErrorResponse = getErrorResponseByCode("010501")
}
