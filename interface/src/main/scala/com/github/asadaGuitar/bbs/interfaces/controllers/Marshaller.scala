package com.github.asadaGuitar.bbs.interfaces.controllers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.asadaGuitar.bbs.interfaces.controllers.models.{ErrorListResponse, ErrorResponse, FindUserByIdSucceededResponse, MessageResponse, PostMessageRequestForm, PostMessageSucceedResponse, PostThreadRequestForm, PostThreadSucceededResponse, SigninRequestForm, SigninUserSucceededResponse, SignupRequestForm, SignupUserSucceededResponse, ThreadResponse, UserIdRequest}
import com.github.asadaGuitar.bbs.domains.models.{EmailAddress, JwtToken, UserId, UserName, UserPassword}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait Marshaller extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val userIdMarshaller: RootJsonFormat[UserId]                                               = jsonFormat1(UserId)

  implicit val userNameMarshaller: RootJsonFormat[UserName]                                           = jsonFormat1(UserName)

  implicit val userPasswordMarshaller: RootJsonFormat[UserPassword]                                   = jsonFormat1(UserPassword)

  implicit val emailAddressMarshaller: RootJsonFormat[EmailAddress]                                   = jsonFormat1(EmailAddress.apply)

  implicit val signinRequestFormMarshaller: RootJsonFormat[SigninRequestForm]                         = jsonFormat2(SigninRequestForm)

  implicit val signupRequestFormMarshaller: RootJsonFormat[SignupRequestForm]                         = jsonFormat4(SignupRequestForm)

  implicit val jwtTokenMarshaller: RootJsonFormat[JwtToken]                                           = jsonFormat1(JwtToken)

  implicit val errorResponseMarshaller: RootJsonFormat[ErrorResponse]                                 = jsonFormat2(ErrorResponse.apply)

  implicit val errorListResponseMarshaller: RootJsonFormat[ErrorListResponse]                         = jsonFormat1(ErrorListResponse)

  implicit val signupUserSucceededResponseMarshaller: RootJsonFormat[SignupUserSucceededResponse]     = jsonFormat1(SignupUserSucceededResponse)

  implicit val signinUserSucceededResponseMarshaller: RootJsonFormat[SigninUserSucceededResponse]     = jsonFormat1(SigninUserSucceededResponse)

  implicit val findUserByIdSucceededResponseMarshaller: RootJsonFormat[FindUserByIdSucceededResponse] = jsonFormat4(FindUserByIdSucceededResponse)

  implicit val userIdRequestMarshaller: RootJsonFormat[UserIdRequest]                                 = jsonFormat1(UserIdRequest)

  implicit val postThreadRequestFormMarshaller: RootJsonFormat[PostThreadRequestForm]                 = jsonFormat2(PostThreadRequestForm)

  implicit val postThreadSucceededResponseMarshaller: RootJsonFormat[PostThreadSucceededResponse]     = jsonFormat1(PostThreadSucceededResponse)

  implicit val threadResponseMarshaller: RootJsonFormat[ThreadResponse]                               = jsonFormat2(ThreadResponse)

  implicit val postMessageRequestFormMarshaller: RootJsonFormat[PostMessageRequestForm]               = jsonFormat1(PostMessageRequestForm)

  implicit val postMessageSucceedResponseMarshaller: RootJsonFormat[PostMessageSucceedResponse]       = jsonFormat1(PostMessageSucceedResponse)

  implicit val messageResponseMarshaller: RootJsonFormat[MessageResponse]                             = jsonFormat2(MessageResponse)
}
