package com.github.asadaGuitar.bbs.interfaces.controllers.validations

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.{provide, reject}
import com.github.asadaGuitar.bbs.interfaces.controllers.models.{PostMessageRequestForm, PostThreadFormWithoutUserId, PostThreadRequestForm, SigninRequestForm, SignupRequestForm}
import com.github.asadaGuitar.bbs.domains.models.{EmailAddress, MessageText, ThreadId, UserId, UserName, UserPassword}
import com.github.asadaGuitar.bbs.usecases.models.{SigninForm, SignupForm}

trait ValidationDirectives {

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

  protected def validateSignupRequestForm(value: SignupRequestForm): Directive1[SignupForm] =
    Validations.validateSignupRequestForm(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validateThreadId(value: String): Directive1[ThreadId] =
    Validations.validateThreadId(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validateSigninRequestForm(value: SigninRequestForm): Directive1[SigninForm] =
    Validations.validateSigninRequestForm(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validatePostThreadRequestForm(value: PostThreadRequestForm): Directive1[PostThreadFormWithoutUserId] =
    Validations.validatePostThreadForm(value).fold(e => reject(ValidationErrorRejection(e)), provide)

  protected def validatePostMessageRequestForm(value: PostMessageRequestForm): Directive1[MessageText] =
    Validations.validatePostMessageForm(value).fold(e => reject(ValidationErrorRejection(e)), provide)
}

