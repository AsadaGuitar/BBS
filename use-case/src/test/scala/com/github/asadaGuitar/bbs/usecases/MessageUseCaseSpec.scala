package com.github.asadaGuitar.bbs.usecases

import com.github.asadaGuitar.bbs.domains.models.{Message, MessageId, MessageText, ThreadId, UserId}
import com.github.asadaGuitar.bbs.repositories.MessagesRepository
import org.scalatest.exceptions.TestFailedException
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.Future

final class MessageUseCaseSpec extends AsyncWordSpec {

  final case class MessageRepositoryTestFailedException() extends Throwable

  private def messagesRepositoryCaseSucceeded: MessagesRepository = new MessagesRepository {

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

  private def messagesRepositoryCaseFailed: MessagesRepository = new MessagesRepository {

    override def save(message: Message): Future[Int] =
      Future.failed(MessageRepositoryTestFailedException())

    override def findAllByThreadId(threadId: ThreadId): Future[Vector[Message]] =
      Future.failed(MessageRepositoryTestFailedException())

    override def existsById(messageId: MessageId): Future[Boolean] =
      Future.failed(MessageRepositoryTestFailedException())
  }

  "MessagesUseCase.generateRandomMessageId" should {

    "generate a random ID of the requested length." in {
      val messageUseCase = new MessageUseCase(messagesRepositoryCaseSucceeded)
      for {
        messageId0 <- messageUseCase.generateRandomMessageId(12)
        messageId1 <- messageUseCase.generateRandomMessageId(12)
      } yield assert{
        (messageId0.value !== messageId1.value) &&
          (messageId1.value.length === 12 && messageId0.value.length === 12)
      }
    }

    "database error messages can be retrieved." in {
      recoverToSucceededIf[MessageRepositoryTestFailedException] {
        new MessageUseCase(messagesRepositoryCaseFailed)
          .generateRandomMessageId(12)
      }
    }
  }

  "MessageUseCase.create" should {

    "returning random message id." in {
      val messageUseCase = new MessageUseCase(messagesRepositoryCaseSucceeded)
      val createCommand =
        MessageUseCase.CreateCommand(
          threadId = ThreadId(Utils.generateRandomString(12)),
          userId = UserId(Utils.generateRandomString(12)),
          text = MessageText(Utils.generateRandomString(8)))

      for {
        m0 <- messageUseCase.create(createCommand)
        m1 <- messageUseCase.create(createCommand)
      } yield assert(m0.value !== m1.value)
    }

    "database error messages can be retrieved." in {
      val createCommand =
        MessageUseCase.CreateCommand(
          threadId = ThreadId(Utils.generateRandomString(12)),
          userId = UserId(Utils.generateRandomString(12)),
          text = MessageText(Utils.generateRandomString(8)))

      recoverToSucceededIf[MessageRepositoryTestFailedException] {
        new MessageUseCase(messagesRepositoryCaseFailed)
          .create(createCommand)
      }
    }
  }

  "MessageUseCase.findAllByThreadId" should {

    "database error messages can be retrieved." in {
      recoverToSucceededIf[MessageRepositoryTestFailedException] {
        new MessageUseCase(messagesRepositoryCaseFailed)
          .findAllByThreadId(ThreadId(Utils.generateRandomString(12)))
      }
    }
  }

  "MessageUseCase" should {

    "can create and search messages." in {

      val messageUseCase = new MessageUseCase(messagesRepositoryCaseSucceeded)

      val threadId0 = ThreadId(Utils.generateRandomString(12))
      val threadId1 = ThreadId(Utils.generateRandomString(12))

      val createCommand0 =
        MessageUseCase.CreateCommand(
          threadId = threadId0,
          userId = UserId(Utils.generateRandomString(12)),
          text = MessageText(Utils.generateRandomString(8)))

      val createCommand1 =
        MessageUseCase.CreateCommand(
          threadId = threadId1,
          userId = UserId(Utils.generateRandomString(12)),
          text = MessageText(Utils.generateRandomString(8)))

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
