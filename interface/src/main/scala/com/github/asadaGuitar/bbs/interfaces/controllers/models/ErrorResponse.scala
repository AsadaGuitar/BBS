package com.github.asadaGuitar.bbs.interfaces.controllers.models

import com.github.asadaGuitar.bbs.interfaces.controllers.MessageProvider

final case class ErrorResponse(code: String, message: String)

final case class ErrorListResponse(errors: List[ErrorResponse])

object ErrorResponse extends MessageProvider {

  private def getErrorResponseByCode(code: String) = ErrorResponse(code, config.getString(code))

  final val userIdValidationError        = getErrorResponseByCode("010101")

  final val userFirstNameValidationError = getErrorResponseByCode("010102")

  final val userLastNameValidationError  = getErrorResponseByCode("010103")

  final val userPasswordValidationError  = getErrorResponseByCode("010104")

  final val notFoundUser                 = getErrorResponseByCode("010105")

  final val emailAddressValidationError  = getErrorResponseByCode("010201")

  final val threadIdValidationError      = getErrorResponseByCode("010301")

  final val threadTitleValidationError   = getErrorResponseByCode("010302")

  final val messageIdValidationError     = getErrorResponseByCode("010401")

  final val messageTextValidationError   = getErrorResponseByCode("010402")

  final val signinFailure                = getErrorResponseByCode("010501")
}