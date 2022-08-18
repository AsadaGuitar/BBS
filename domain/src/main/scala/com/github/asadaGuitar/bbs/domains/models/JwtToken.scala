package com.github.asadaGuitar.bbs.domains.models

object JwtToken {

  private val jwtTokenPattern = "^[a-zA-Z\\d].*\\.[a-zA-Z\\d].*\\.[a-zA-Z\\d].*$".r

  def validateJwtToken(value: String): Boolean = {
    jwtTokenPattern.matches(value)
  }
}

final case class JwtToken(value: String) {
  import JwtToken._
  require(validateJwtToken(value))
}
