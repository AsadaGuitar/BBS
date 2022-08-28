package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import cats.implicits.catsSyntaxFlatMapOps
import com.github.asadaGuitar.bbs.domains.models.{Message, MessageId, MessageText, ThreadId, UserId}
import com.github.asadaGuitar.bbs.interfaces.Utils
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao.Tables
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatest.wordspec.AsyncWordSpec
import slick.migration.api.{PostgresDialect, TableMigration}

import scala.language.postfixOps

final class SlickMessagesRepositoryImplSpec
  extends AsyncWordSpec
    with BeforeAndAfter
    with BeforeAndAfterAll
    with SlickDbConfigProvider
    with DbColumnsConvertor {

  implicit val dialect: PostgresDialect = new PostgresDialect
  import dbConfig.profile.api._

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
    val migration = dropMessages
    dbConfig.db.run(migration())
  }

  override protected def afterAll(): Unit = {
    val deleteMessage = sql"""DELETE FROM messages;"""
    dbConfig.db.run(deleteMessage.asUpdate)
  }

  final val messagesRepositoryImpl = new SlickMessagesRepositoryImpl


  "SlickMessageRepositoryImpl.save" should {

    "`Message` object can be created successfully." in {
      val testData =
        Message(
          id = MessageId(Utils.generateRandomString(12)),
          threadId = ThreadId(Utils.generateRandomString(12)),
          userId = UserId(Utils.generateRandomString(12)),
          text = MessageText(Utils.generateRandomString(25))
        )

      messagesRepositoryImpl.save(testData).map(n => assert(n === 1))
    }

    "cannot be created if the same ID already exists." in {

      val sameMessageId = MessageId(Utils.generateRandomString(12))

      val testData0 =
        Message(
          id = sameMessageId,
          threadId = ThreadId(Utils.generateRandomString(12)),
          userId = UserId(Utils.generateRandomString(12)),
          text = MessageText(Utils.generateRandomString(25))
        )

      val testData1 =
        Message(
          id = sameMessageId,
          threadId = ThreadId(Utils.generateRandomString(12)),
          userId = UserId(Utils.generateRandomString(12)),
          text = MessageText(Utils.generateRandomString(25))
        )

      recoverToSucceededIf[SlickException] {
        messagesRepositoryImpl.save(testData0) >>
          messagesRepositoryImpl.save(testData1)
      }
    }
  }

  "SlickMessagesRepositoryImpl" should {

    "any messages with the same stored thread ID can be retrieved." in {

      val threadId = ThreadId(Utils.generateRandomString(12))

      val message0 =
        Message(
          id = MessageId(Utils.generateRandomString(12)),
          threadId = threadId,
          userId = UserId(Utils.generateRandomString(12)),
          text = MessageText(Utils.generateRandomString(25))
        )

      val message1 =
        Message(
          id = MessageId(Utils.generateRandomString(12)),
          threadId = threadId,
          userId = UserId(Utils.generateRandomString(12)),
          text = MessageText(Utils.generateRandomString(25))
        )

      for {
        _ <- {
          messagesRepositoryImpl.save(message0) >>
            messagesRepositoryImpl.save(message1)
        }
        messages <- messagesRepositoryImpl.findAllByThreadId(threadId)
      } yield {
        assert{
          messages.exists(_ === message0) &&
            messages.exists(_ === message1) &&
            messages.length === 2
        }
      }
    }

    "is possible to check whether the stored message exists or not." in {

      val messageId = MessageId(Utils.generateRandomString(12))

      val message =
        Message(
          id = messageId,
          threadId = ThreadId(Utils.generateRandomString(12)),
          userId = UserId(Utils.generateRandomString(12)),
          text = MessageText(Utils.generateRandomString(25))
        )

      for {
        _ <- messagesRepositoryImpl.save(message)
        isExists0 <- messagesRepositoryImpl.existsById(messageId)
        isExists1 <- messagesRepositoryImpl.existsById(MessageId(Utils.generateRandomString(12)))
      } yield assert(isExists0 && !isExists1)
    }
  }
}
