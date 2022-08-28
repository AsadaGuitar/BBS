package com.github.asadaGuitar.bbs.domains.models

import org.apache.commons.validator.routines.EmailValidator

object EmailAddress {

  private val validator: EmailValidator = EmailValidator.getInstance()
}

final case class EmailAddress(value: String) {

  import EmailAddress._

  require(validator.isValid(value))
}
