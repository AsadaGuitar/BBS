package com.github.asadaGuitar.bbs.interfaces.controllers.validations

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.{ provide, reject }
import com.github.asadaGuitar.bbs.interfaces.controllers.models.{
  PostMessageRequest,
  PostThreadFormWithoutUserId,
  PostThreadRequest,
  SigninRequest,
  SignupRequest
}
import com.github.asadaGuitar.bbs.domains.models.{ EmailAddress, MessageText, ThreadId, UserId, UserName, UserPassword }
import com.github.asadaGuitar.bbs.usecases.UsersUseCase.{ SigninCommand, SignupCommand }

trait ValidationDirectives {

  protected def validateSignupRequest(value: SignupRequest): Directive1[SignupCommand] =
    Validations.validateSignupRequest(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validateSigninRequest(value: SigninRequest): Directive1[SigninCommand] =
    Validations.validateSigninRequest(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validateUserId(value: String): Directive1[UserId] =
    Validations.validateUserId(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validateUserFirstName(value: String): Directive1[UserName] =
    Validations.validateUserFirstName(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validateUserLastName(value: String): Directive1[UserName] =
    Validations.validateUserLastName(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validateUserPassword(value: String): Directive1[UserPassword] =
    Validations.validateUserPassword(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validateEmailAddress(value: String): Directive1[EmailAddress] =
    Validations.validateEmailAddress(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validateThreadId(value: String): Directive1[ThreadId] =
    Validations.validateThreadId(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validatePostThreadRequest(value: PostThreadRequest): Directive1[PostThreadFormWithoutUserId] =
    Validations.validatePostThreadRequest(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validatePostMessageRequest(value: PostMessageRequest): Directive1[MessageText] =
    Validations.validatePostMessage(value).fold(e => reject(ValidationErrorRejection(e)), provide)
}
