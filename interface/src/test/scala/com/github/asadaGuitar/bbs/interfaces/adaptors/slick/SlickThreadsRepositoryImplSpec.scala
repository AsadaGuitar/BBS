package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import cats.implicits.catsSyntaxFlatMapOps
import com.github.asadaGuitar.bbs.domains.models.{Thread, ThreadId, ThreadTitle, UserId}
import com.github.asadaGuitar.bbs.interfaces.Utils
import com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao.Tables
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll}
import org.scalatest.wordspec.AsyncWordSpec
import slick.migration.api.{PostgresDialect, TableMigration}

final class SlickThreadsRepositoryImplSpec
  extends AsyncWordSpec
  with BeforeAndAfter
  with BeforeAndAfterAll
  with SlickDbConfigProvider
  with DbColumnsConvertor {

  implicit val dialect: PostgresDialect = new PostgresDialect
  import dbConfig.profile.api._

  // At the start of the test,
  // the `threads` table is created and test data is submitted.
  before {
    val createMessages =
      TableMigration(Tables.Threads).create
        .addColumns(_.id, _.userId, _.title, _.isClose, _.createAt, _.modifyAt, _.closeAt)

    val migration = createMessages
    dbConfig.db.run(migration())
  }

  // Discard the `threads` table used in the test at the end of the test.
  after {
    val dropMessages = TableMigration(Tables.Threads).drop
    val migration = dropMessages
    dbConfig.db.run(migration())
  }

  // Delete all records in the database after each test.
  override protected def afterAll(): Unit = {
    val deleteMessage = sql"""DELETE FROM threads;"""
    dbConfig.db.run(deleteMessage.asUpdate)
  }

  final val threadsRepositoryImpl = new SlickThreadsRepositoryImpl

  "SlickThreadsRepositoryImpl" should {

    "stored data can be retrieved by specifying an ID." in {
      val threadId = ThreadId(Utils.generateRandomString(12))
      val thread =
        Thread(
          id = threadId,
          userId = UserId(Utils.generateRandomString(12)),
          title = ThreadTitle(Utils.generateRandomString(8)))

      for {
        _ <- threadsRepositoryImpl.save(thread)
        t <- threadsRepositoryImpl.findById(threadId)
      } yield t match {
        case Some(value) => assert(value === thread)
        case None => fail()
      }
    }

    "returns `Option.None` if no data exists when an attempt is made to retrieve it by specifying an ID." in {
      val threadId = ThreadId(Utils.generateRandomString(12))
      threadsRepositoryImpl.findById(threadId).map(t => assert(t.isEmpty))
    }

    "multiple stored data can be retrieved by specifying ID." in {
      val userId = UserId(Utils.generateRandomString(12))

      val thread0 =
        Thread(
          id = ThreadId(Utils.generateRandomString(12)),
          userId = userId,
          title = ThreadTitle(Utils.generateRandomString(8)))

      val thread1 =
        Thread(
          id = ThreadId(Utils.generateRandomString(12)),
          userId = userId,
          title = ThreadTitle(Utils.generateRandomString(8)))

      for {
        _ <- {
          threadsRepositoryImpl.save(thread0) >>
            threadsRepositoryImpl.save(thread1)
        }
        threads <- threadsRepositoryImpl.findAllByUserId(userId)
      } yield assert {
        threads.exists(_ === thread0) &&
          threads.exists(_ === thread1) &&
          threads.length === 2
      }
    }

    "when multiple data are attempted to be retrieved, `Vector.empty` is returned if no data exist." in {
      val userId = UserId(Utils.generateRandomString(12))
      threadsRepositoryImpl.findAllByUserId(userId).map(threads => assert(threads.isEmpty))
    }

    "is possible to obtain the ID whether it exists or not by specifying the ID." in {
      val threadId = ThreadId(Utils.generateRandomString(12))
      val thread =
        Thread(
          id = threadId,
          userId = UserId(Utils.generateRandomString(12)),
          title = ThreadTitle(Utils.generateRandomString(8)))

      threadsRepositoryImpl.save(thread) >>
        threadsRepositoryImpl.existsById(threadId).map(assert(_))
    }
  }
}
