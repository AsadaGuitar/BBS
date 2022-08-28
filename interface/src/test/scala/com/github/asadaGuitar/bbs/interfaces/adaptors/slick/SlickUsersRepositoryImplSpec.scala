package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import cats.implicits.catsSyntaxFlatMapOps
import com.github.asadaGuitar.bbs.domains.models.{BcryptedPassword, EmailAddress, User, UserId, UserName, UserPassword}
import com.github.asadaGuitar.bbs.interfaces.Utils
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao.Tables
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatest.wordspec.AsyncWordSpec
import slick.migration.api.{PostgresDialect, TableMigration}

final class SlickUsersRepositoryImplSpec
  extends AsyncWordSpec
    with BeforeAndAfter
    with BeforeAndAfterAll
    with SlickDbConfigProvider
    with DbColumnsConvertor {

  implicit val dialect: PostgresDialect = new PostgresDialect

  import dbConfig.profile.api._

  // At the start of the test,
  // the `users` table is created and test data is submitted.
  before {
    val createMessages =
      TableMigration(Tables.Users).create
        .addColumns(_.id, _.firstName, _.lastName, _.emailAddress, _.password, _.isClose, _.createAt, _.modifyAt, _.closeAt)

    val migration = createMessages
    dbConfig.db.run(migration())
  }

  // Discard the `users` table used in the test at the end of the test.
  after {
    val dropMessages = TableMigration(Tables.Users).drop
    val migration = dropMessages
    dbConfig.db.run(migration())
  }

  // Delete all records in the database after each test.
  override protected def afterAll(): Unit = {
    val deleteMessage = sql"""DELETE FROM users;"""
    dbConfig.db.run(deleteMessage.asUpdate)
  }

  final val usersRepositoryImpl = new SlickUsersRepositoryImpl

  "SlickUsersRepositoryImpl" should {

    "stored data can be acquired by specifying ID." in {
      val userId = UserId(Utils.generateRandomString(12))
      val user =
        User(
          id = userId,
          firstName = UserName(Utils.generateRandomString(8)),
          lastName = UserName(Utils.generateRandomString(8)),
          emailAddress = EmailAddress("info@sample.com"),
          password = BcryptedPassword("Passw0rd"))

      usersRepositoryImpl.save(user) >>
        usersRepositoryImpl.findById(userId).map {
          case Some(value) => assert(value === user)
          case None => fail()
        }
    }

    "returns Option.None if the data does not exist when trying to retrieve data by specifying an ID." in {
      val userId = UserId(Utils.generateRandomString(12))

      usersRepositoryImpl.findById(userId).map(user => assert(user.isEmpty))
    }

    "is possible to check whether data with a specified ID exists." in {
      val userId = UserId(Utils.generateRandomString(12))
      val user =
        User(
          id = userId,
          firstName = UserName(Utils.generateRandomString(8)),
          lastName = UserName(Utils.generateRandomString(8)),
          emailAddress = EmailAddress("sample@info.com"),
          password = BcryptedPassword("Passw0rd"))

      for {
        _ <- usersRepositoryImpl.save(user)
        isExists0 <- usersRepositoryImpl.existsById(userId)
        isExists1 <- usersRepositoryImpl.existsById(UserId(Utils.generateRandomString(12)))
      } yield assert(isExists0, !isExists1)
    }

    "is possible to check whether data with a specified email address exists." in {
      val emailAddress = EmailAddress("sample0@info.com")
      val user =
        User(
          id = UserId(Utils.generateRandomString(12)),
          firstName = UserName(Utils.generateRandomString(8)),
          lastName = UserName(Utils.generateRandomString(8)),
          emailAddress = emailAddress,
          password = BcryptedPassword("Passw0rd"))

      for {
        _ <- usersRepositoryImpl.save(user)
        isExists0 <- usersRepositoryImpl.existsByEmailAddress(emailAddress)
        isExists1 <- usersRepositoryImpl.existsByEmailAddress(EmailAddress("sample1@info.com"))
      } yield assert(isExists0, !isExists1)
    }
  }
}
