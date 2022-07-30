package com.github.asadaGuitar.bbs.interfaces.controllers.models

final case class SignupRequestForm(first_name: String,
                                   last_name: String,
                                   email_address: String,
                                   password: String)
