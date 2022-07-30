package com.github.asadaGuitar.bbs.usecases.models

import com.github.asadaGuitar.bbs.domains.models.{UserId, UserPassword}

final case class SigninForm(userId: UserId,
                            password: UserPassword)
