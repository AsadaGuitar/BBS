package com.github.asadaGuitar.bbs.domains.models

final case class JwtToken(value: String) {
  require(value.nonEmpty)
}
