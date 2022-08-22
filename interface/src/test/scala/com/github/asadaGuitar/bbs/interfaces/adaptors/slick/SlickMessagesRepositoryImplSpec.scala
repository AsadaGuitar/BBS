package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import cats.implicits.catsSyntaxFlatMapOps
import com.github.asadaGuitar.bbs.domains.models.{ Message, MessageId, MessageText, ThreadId, UserId }
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao.Tables
import org.scalatest.BeforeAndAfter
import org.scalatest.wordspec.AsyncWordSpec
import slick.SlickException
import slick.migration.api.{ PostgresDialect, TableMigration }

import scala.language.postfixOps
import scala.util.Random

final class SlickMessagesRepositoryImplSpec
    extends AsyncWordSpec
    with BeforeAndAfter
    with SlickDbConfigProvider
    with DbColumnsConvertor {

  implicit val dialect: PostgresDialect = new PostgresDialect

  // At the start of the test,
  // the `messages` table is created and test data is submitted.
  before {
    val createMessages =
      TableMigration(Tables.Messages).create
        .addColumns(_.id, _.threadId, _.userId, _.text, _.isClose, _.createAt, _.modifyAt, _.closeAt)

    val migration = createMessages
    dbConfig.db.run(migration())
  }

  // Discard the `messages` table used in the test at the end of the test.
  after {
    val dropMessages = TableMigration(Tables.Messages).drop
    val migration    = dropMessages
    dbConfig.db.run(migration())
  }

  final val messagesRepositoryImpl              = new SlickMessagesRepositoryImpl
  private def generateRandomString(length: Int) = Random.alphanumeric.take(length).mkString

  "SlickMessageRepositoryImpl.save" should {

    "`Message` object can be created successfully." in {
      val testData =
        Message(
          id = MessageId(generateRandomString(15)),
          threadId = ThreadId(generateRandomString(12)),
          userId = UserId(generateRandomString(12)),
          text = MessageText(generateRandomString(25))
        )

      messagesRepositoryImpl.save(testData).map(n => assert(n === 1))
    }

    "cannot be created if the same ID already exists." in {

      val sameMessageId = MessageId(generateRandomString(12))

      val testData0 =
        Message(
          id = sameMessageId,
          threadId = ThreadId(generateRandomString(12)),
          userId = UserId(generateRandomString(12)),
          text = MessageText(generateRandomString(25))
        )

      val testData1 =
        Message(
          id = sameMessageId,
          threadId = ThreadId(generateRandomString(12)),
          userId = UserId(generateRandomString(12)),
          text = MessageText(generateRandomString(25))
        )

      recoverToSucceededIf[SlickException] {
        messagesRepositoryImpl.save(testData0) >>
        messagesRepositoryImpl.save(testData1)
      }
    }

  }
}
