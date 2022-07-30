package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import com.github.asadaGuitar.bbs.domains.models.{EmailAddress, User, UserId, UserName, UserPassword}
import com.github.asadaGuitar.bbs.repositories.UsersRepository
import com.github.asadaGuitar.bbs.repositories.models.UserForm

import java.sql.Date
import java.util.{Date => UtilDate}
import scala.concurrent.{ExecutionContext, Future}

final class SlickUsersRepositoryImpl(implicit ec: ExecutionContext) extends SlickDbConfigProvider with UsersRepository {
  import dbConfig.profile.api._

  override def save(userForm: UserForm): Future[Int] =
    userForm match {
      case UserForm(id, emailAddress, firstName, lastName, password) =>
        dbConfig.db.run {
          UsersTable.table.insertOrUpdate(
            UsersRecord(
              id = id.value,
              first_name = firstName.value,
              last_name = lastName.value,
              email_address = emailAddress.value,
              password = password.value
            )
          )
        }
    }

  override def findById(userId: UserId): Future[Option[User]] =
    for {
      record <- dbConfig.db.run {
        UsersTable.table
          .filter(_.id === userId.value)
          .result
          .headOption
      }
    } yield record.map {
      case UsersRecord(id, first_name, last_name, email_address, password, is_close, create_at, modify_at, close_at) =>
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
    dbConfig.db.run(UsersTable.table.filter(_.id === userId.value).exists.result)

}

private[adaptors] case class UsersRecord(id: String,
                                         first_name: String,
                                         last_name: String,
                                         email_address: String,
                                         password: String,
                                         is_close: Boolean = false,
                                         create_at: Date = {
                                           val now = new UtilDate()
                                           new Date(now.getTime)
                                         },
                                         modify_at: Option[Date] = None,
                                         close_at: Option[Date] = None)

private[adaptors] object UsersTable extends SlickDbConfigProvider {
  import dbConfig.profile.api._

  val table = TableQuery[UsersTable]

  private[adaptors] class UsersTable(tag: Tag) extends Table[UsersRecord](tag, "users") {

    def id = column[String]("id")

    def firstName = column[String]("first_name")

    def lastName = column[String]("last_name")

    def emailAddress = column[String]("email_address")

    def password = column[String]("password")

    def isClose = column[Boolean]("is_close")

    def createAt = column[java.sql.Date]("create_at")

    def modifyAt = column[Option[java.sql.Date]]("modify_at")

    def closeAt = column[Option[java.sql.Date]]("close_at")

    override def * =
      (id, firstName, lastName, emailAddress, password, isClose, createAt, modifyAt, closeAt) <>
        ((UsersRecord.apply _).tupled, UsersRecord.unapply)
  }
}

