package com.github.asadaGuitar.bbs.domains.models

import com.typesafe.config.ConfigFactory

object Jwt {

  private val patternRequired =
    ConfigFactory.load().getString("application.domain.jwt.pattern").r

  def matches(value: String): Boolean =
    patternRequired.matches(value)
}

final case class Jwt(value: String) {

  import Jwt._

  require(matches(value))
}
