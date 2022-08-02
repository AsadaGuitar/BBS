package com.github.asadaGuitar.bbs.interfaces.controllers.models

final case class FindUserByIdSucceededResponse(
    user_account_id: String,
    first_name: String,
    last_name: String,
    email_address: String
)
