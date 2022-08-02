package com.github.asadaGuitar.bbs.repositories.models

import com.github.asadaGuitar.bbs.domains.models.{ EmailAddress, UserId, UserName, UserPassword }

final case class UserForm(
    id: UserId,
    emailAddress: EmailAddress,
    firstName: UserName,
    lastName: UserName,
    password: UserPassword
)
