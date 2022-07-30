package com.github.asadaGuitar.bbs.usecases.models

import com.github.asadaGuitar.bbs.domains.models.{EmailAddress, UserName, UserPassword}

final case class SignupForm(emailAddress: EmailAddress,
                            firstName: UserName,
                            lastName: UserName,
                            password: UserPassword)
