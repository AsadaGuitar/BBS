package com.github.asadaGuitar.bbs.usecases

import com.github.asadaGuitar.bbs.domains.models.{Message, MessageId, MessageText, ThreadId, UserId}
import com.github.asadaGuitar.bbs.repositories.MessagesRepository
import org.scalatest.exceptions.TestFailedException
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.Future

final class MessageUseCaseSpec extends AsyncWordSpec {

  private val messagesRepositoryCaseSucceeded: MessagesRepository = new MessagesRepository {

    private var database: Vector[Message] = Vector.empty

    override def save(message: Message): Future[Int] =
      Future.successful{
        database = database :+ message
        1
      }

    override def findAllByThreadId(threadId: ThreadId): Future[Vector[Message]] =
      Future.successful(database.filter(_.threadId.value === threadId.value))

    override def existsById(messageId: MessageId): Future[Boolean] = {
      Future(database.exists(_.id.value === messageId.value))
    }
  }

  private val messagesRepositoryCaseFailed: MessagesRepository = new MessagesRepository {

    override def save(message: Message): Future[Int] = Future.failed(new Throwable("failed!"))

    override def findAllByThreadId(threadId: ThreadId): Future[Vector[Message]] =
      Future.failed(new Throwable("failed!"))

    override def existsById(messageId: MessageId): Future[Boolean] = Future.failed(new Throwable("failed!"))
  }

  "MessagesUseCase.generateRandomMessageId" should {

    "generate a random ID of the requested length." in {
      val messageUseCase = new MessageUseCase(messagesRepositoryCaseSucceeded)
      for {
        messageId0 <- messageUseCase.generateRandomMessageId(15)
        messageId1 <- messageUseCase.generateRandomMessageId(15)
      } yield assert{
        (messageId0.value !== messageId1.value) &&
          (messageId1.value.length === 15 && messageId0.value.length === 15)
      }
    }

    "database error messages can be retrieved." in {
      new MessageUseCase(messagesRepositoryCaseFailed)
        .generateRandomMessageId(15)
        .map(_ => fail())
        .recover(e => assert(!e.isInstanceOf[TestFailedException]))
    }
  }

  "MessageUseCase.create" should {

    val createCommand =
      MessageUseCase.CreateCommand(
        threadId = ThreadId("THREAD000001"),
        userId = UserId("USER000001"),
        text = MessageText("TEST"))

    "returning random message id." in {
      val messageUseCase = new MessageUseCase(messagesRepositoryCaseSucceeded)
      for {
        m0 <- messageUseCase.create(createCommand)
        m1 <- messageUseCase.create(createCommand)
      } yield assert(m0.value !== m1.value)
    }

    "database error messages can be retrieved." in {
      new MessageUseCase(messagesRepositoryCaseFailed)
        .create(createCommand)
        .map(_ => fail())
        .recover(e => assert(!e.isInstanceOf[TestFailedException]))
    }
  }

  "MessageUseCase.findAllByThreadId" should {

    "database error messages can be retrieved." in {
      new MessageUseCase(messagesRepositoryCaseFailed)
        .findAllByThreadId(ThreadId("TESTCOMMAND"))
        .map(_ => fail())
        .recover(e => assert(!e.isInstanceOf[TestFailedException]))
    }
  }

  "MessageUseCase" should {

    "can create and search messages." in {

      val messageUseCase = new MessageUseCase(messagesRepositoryCaseSucceeded)

      val threadId0 = ThreadId("THREAD000001")
      val threadId1 = ThreadId("THREAD000002")

      val createCommand0 =
        MessageUseCase.CreateCommand(
          threadId = threadId0,
          userId = UserId("USER000002"),
          text = MessageText("TEST"))

      val createCommand1 =
        MessageUseCase.CreateCommand(
          threadId = threadId1,
          userId = UserId("USER000003"),
          text = MessageText("TEST"))

      for {
        _ <- messageUseCase.create(createCommand0)
        _ <- {
          val task = messageUseCase.create(createCommand1)
          Thread.sleep(500)
          task
        }
        message0 <- messageUseCase.findAllByThreadId(threadId0)
        message1 <- messageUseCase.findAllByThreadId(threadId1)
      } yield {
        assert {
          message0.exists(_.threadId.value === threadId0.value) &&
            message1.exists(_.threadId.value === threadId1.value)
        }
      }
    }
  }
}
