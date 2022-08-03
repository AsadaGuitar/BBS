package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import com.github.asadaGuitar.bbs.domains.models.{ EmailAddress, User, UserId, UserName, UserPassword }
import com.github.asadaGuitar.bbs.domains.models.{ EmailAddress, User, UserId, UserName, UserPassword }
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao.Tables
import com.github.asadaGuitar.bbs.repositories.UsersRepository
import com.github.asadaGuitar.bbs.repositories.models.UserForm

import java.sql.Timestamp
import java.util.Date
import scala.concurrent.{ ExecutionContext, Future }

final class SlickUsersRepositoryImpl(implicit ec: ExecutionContext) extends SlickDbConfigProvider with UsersRepository {
  import dbConfig.profile.api._

  override def save(userForm: UserForm): Future[Int] = {
    val UserForm(id, emailAddress, firstName, lastName, password) = userForm
    dbConfig.db.run {
      Tables.Users
        .insertOrUpdate(
          Tables.UsersRow(
            id = id.value,
            firstName = firstName.value,
            lastName = lastName.value,
            emailAddress = emailAddress.value,
            password = password.value,
            createAt = new Timestamp(new Date().getTime)
          )
        )
    }
  }

  override def findById(userId: UserId): Future[Option[User]] =
    for {
      rowOption <- dbConfig.db.run {
        Tables.Users
          .filter(_.id === userId.value)
          .result
          .headOption
      }
    } yield rowOption.map { row =>
      val Tables.UsersRow(
        id,
        first_name,
        last_name,
        email_address,
        password,
        is_close,
        create_at,
        modify_at,
        close_at
      ) = row
      User(
        id = UserId(id),
        firstName = UserName(first_name),
        lastName = UserName(last_name),
        emailAddress = EmailAddress(email_address),
        password = UserPassword(password),
        isClose = is_close,
        createAt = create_at,
        modifyAt = modify_at,
        closeAt = close_at
      )
    }

  override def existsById(userId: UserId): Future[Boolean] =
    dbConfig.db.run {
      Tables.Users
        .filter(_.id === userId.value)
        .exists
        .result
    }

  override def existsByEmailAddress(mailAddress: EmailAddress): Future[Boolean] =
    dbConfig.db.run {
      Tables.Users
        .filter(_.emailAddress === mailAddress.value)
        .exists
        .result
    }
}
