package com.github.asadaGuitar.bbs.interfaces.controllers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.asadaGuitar.bbs.interfaces.controllers.models.{
  ErrorListResponse,
  ErrorResponse,
  FindMessageByThreadIdSucceededResponse,
  FindThreadByUserIdSucceededResponse,
  FindUserByIdSucceededResponse,
  PostMessageRequest,
  PostMessageSucceededResponse,
  PostThreadRequest,
  PostThreadSucceededResponse,
  SigninRequest,
  SigninSucceededResponse,
  SignupRequest,
  SignupSucceededResponse,
  UserIdRequest
}
import com.github.asadaGuitar.bbs.domains.models.{ EmailAddress, Jwt, UserId, UserName, UserPassword }
import spray.json.{ DefaultJsonProtocol, RootJsonFormat }

trait Marshaller extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val userIdMarshaller: RootJsonFormat[UserId] = jsonFormat1(UserId)

  implicit val userNameMarshaller: RootJsonFormat[UserName] = jsonFormat1(UserName)

  implicit val userPasswordMarshaller: RootJsonFormat[UserPassword] = jsonFormat1(UserPassword)

  implicit val emailAddressMarshaller: RootJsonFormat[EmailAddress] = jsonFormat1(EmailAddress.apply)

  implicit val signinRequestFormMarshaller: RootJsonFormat[SigninRequest] = jsonFormat2(SigninRequest)

  implicit val signupRequestFormMarshaller: RootJsonFormat[SignupRequest] = jsonFormat4(SignupRequest)

  implicit val jwtTokenMarshaller: RootJsonFormat[Jwt] = jsonFormat1(Jwt.apply)

  implicit val errorResponseMarshaller: RootJsonFormat[ErrorResponse] = jsonFormat2(ErrorResponse.apply)

  implicit val errorListResponseMarshaller: RootJsonFormat[ErrorListResponse] = jsonFormat1(ErrorListResponse)

  implicit val signupUserSucceededResponseMarshaller: RootJsonFormat[SignupSucceededResponse] = jsonFormat1(
    SignupSucceededResponse
  )

  implicit val signinUserSucceededResponseMarshaller: RootJsonFormat[SigninSucceededResponse] = jsonFormat1(
    SigninSucceededResponse
  )

  implicit val findUserByIdSucceededResponseMarshaller: RootJsonFormat[FindUserByIdSucceededResponse] = jsonFormat4(
    FindUserByIdSucceededResponse
  )

  implicit val userIdRequestMarshaller: RootJsonFormat[UserIdRequest] = jsonFormat1(UserIdRequest)

  implicit val postThreadRequestFormMarshaller: RootJsonFormat[PostThreadRequest] = jsonFormat2(
    PostThreadRequest
  )

  implicit val postThreadSucceededResponseMarshaller: RootJsonFormat[PostThreadSucceededResponse] = jsonFormat1(
    PostThreadSucceededResponse
  )

  implicit val threadResponseMarshaller: RootJsonFormat[FindThreadByUserIdSucceededResponse] = jsonFormat2(
    FindThreadByUserIdSucceededResponse
  )

  implicit val postMessageRequestFormMarshaller: RootJsonFormat[PostMessageRequest] = jsonFormat1(
    PostMessageRequest
  )

  implicit val postMessageSucceedResponseMarshaller: RootJsonFormat[PostMessageSucceededResponse] = jsonFormat1(
    PostMessageSucceededResponse
  )

  implicit val messageResponseMarshaller: RootJsonFormat[FindMessageByThreadIdSucceededResponse] = jsonFormat2(
    FindMessageByThreadIdSucceededResponse
  )
}
