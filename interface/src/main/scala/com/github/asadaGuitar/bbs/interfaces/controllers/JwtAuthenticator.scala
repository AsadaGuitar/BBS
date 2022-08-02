package com.github.asadaGuitar.bbs.interfaces.controllers

import akka.http.scaladsl.server.Directive1
import com.emarsys.jwt.akka.http.{ JwtAuthentication, JwtConfig }
import com.github.asadaGuitar.bbs.domains.models.{ JwtToken, UserId }
import com.typesafe.config.Config

trait JwtAuthenticator extends Marshaller {

  implicit val config: Config

  protected val jwtAuth: JwtAuthentication = new JwtAuthentication {
    override lazy val jwtConfig: JwtConfig = new JwtConfig(config)
  }

  def generateToken(userId: UserId): JwtToken = JwtToken(jwtAuth.generateToken(userId))

  def authenticate: Directive1[UserId] =
    jwtAuth.jwtAuthenticate(jwtAuth.as[UserId])
}
