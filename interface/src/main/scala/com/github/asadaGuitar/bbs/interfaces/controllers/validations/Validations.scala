package com.github.asadaGuitar.bbs.interfaces.controllers.validations

import cats.data.ValidatedNel
import cats.implicits.{catsSyntaxTuple2Semigroupal, catsSyntaxTuple4Semigroupal, catsSyntaxValidatedId, toTraverseOps}
import com.github.asadaGuitar.bbs.interfaces.controllers.models.{ErrorResponse, PostMessageRequest, PostThreadFormWithoutUserId, PostThreadRequest, SigninRequest, SignupRequest, UserIdRequest}
import com.github.asadaGuitar.bbs.domains.models.{EmailAddress, MessageText, PlainPassword, ThreadId, ThreadTitle, UserId, UserName}
import com.github.asadaGuitar.bbs.usecases.UsersUseCase.{SigninCommand, SignupCommand}

object Validations {

  def validateSignupRequest(value: SignupRequest): ValidatedNel[ErrorResponse, SignupCommand] = {
    val SignupRequest(first_name, last_name, email_address, password) = value
    (
      validateEmailAddress(email_address),
      validateUserFirstName(first_name),
      validateUserLastName(last_name),
      validateUserPassword(password)
    ).mapN(SignupCommand)
  }

  def validateSigninRequest(value: SigninRequest): ValidatedNel[ErrorResponse, SigninCommand] = {
    val SigninRequest(email_address, password) = value
    (validateUserId(email_address), validateUserPassword(password))
      .mapN(SigninCommand)
  }

  def validateUserId(value: String): ValidatedNel[ErrorResponse, UserId] =
    try {
      UserId(value).validNel
    } catch {
      case _: IllegalArgumentException =>
        ErrorResponse.userIdValidationError.invalidNel
    }

  def validateUserFirstName(value: String): ValidatedNel[ErrorResponse, UserName] =
    try {
      UserName(value).validNel
    } catch {
      case _: IllegalArgumentException =>
        ErrorResponse.userFirstNameValidationError.invalidNel
    }

  def validateUserLastName(value: String): ValidatedNel[ErrorResponse, UserName] =
    try {
      UserName(value).validNel
    } catch {
      case _: IllegalArgumentException =>
        ErrorResponse.userLastNameValidationError.invalidNel
    }

  def validateEmailAddress(value: String): ValidatedNel[ErrorResponse, EmailAddress] =
    try {
      EmailAddress(value).validNel
    } catch {
      case _: IllegalArgumentException =>
        ErrorResponse.emailAddressValidationError.invalidNel
    }

  def validateUserPassword(value: String): ValidatedNel[ErrorResponse, PlainPassword] =
    try {
      PlainPassword(value).validNel
    } catch {
      case _: IllegalArgumentException =>
        ErrorResponse.userPasswordValidationError.invalidNel
    }

  def validateThreadTitle(value: String): ValidatedNel[ErrorResponse, ThreadTitle] =
    try {
      ThreadTitle(value).validNel
    } catch {
      case _: IllegalArgumentException =>
        ErrorResponse.threadTitleValidationError.invalidNel
    }

  def validateUserIdRequest(value: UserIdRequest): ValidatedNel[ErrorResponse, UserId] =
    try {
      UserId(value.user_id).validNel
    } catch {
      case _: IllegalArgumentException =>
        ErrorResponse.userIdValidationError.invalidNel
    }

  def validateThreadId(value: String): ValidatedNel[ErrorResponse, ThreadId] =
    try {
      ThreadId(value).validNel
    } catch {
      case _: IllegalArgumentException =>
        ErrorResponse.threadIdValidationError.invalidNel
    }

  def validateMessageText(value: String): ValidatedNel[ErrorResponse, MessageText] =
    try {
      MessageText(value).validNel
    } catch {
      case _: IllegalArgumentException =>
        ErrorResponse.messageTextValidationError.invalidNel
    }

  def validatePostThreadRequest(value: PostThreadRequest): ValidatedNel[ErrorResponse, PostThreadFormWithoutUserId] = {
    val PostThreadRequest(title, ids) = value
    (validateThreadTitle(title), ids.map(validateUserIdRequest).sequence)
      .mapN(PostThreadFormWithoutUserId)
  }

  def validatePostMessage(value: PostMessageRequest): ValidatedNel[ErrorResponse, MessageText] = {
    val PostMessageRequest(message) = value
    validateMessageText(message)
  }
}
