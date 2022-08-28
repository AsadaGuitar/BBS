package com.github.asadaGuitar.bbs.usecases

import com.github.asadaGuitar.bbs.domains.models
import com.github.asadaGuitar.bbs.domains.models.{ThreadId, ThreadTitle, UserId, UserThreads}
import com.github.asadaGuitar.bbs.domains.repositories.{ThreadsRepository, UserThreadsRepository}
import org.scalatest.wordspec.AsyncWordSpec

import scala.concurrent.Future

final class ThreadUseCaseSpec extends AsyncWordSpec {

  trait ThreadUseCaseTestException extends Throwable

  final case class UserThreadsRepositoryTestFailedException() extends ThreadUseCaseTestException

  final case class ThreadRepositoryTestFailedException() extends ThreadUseCaseTestException

  private def userThreadsRepositoryCaseSucceeded = new UserThreadsRepository {

    private var database: Vector[UserThreads] = Vector.empty

    override def save(userThreads: UserThreads): Future[Int] =
      Future.successful {
        database = database :+ userThreads
        1
      }

    override def findAllByUserId(userId: UserId): Future[Vector[UserThreads]] =
      Future.successful(database.filter(_.userId === userId))
  }

  private def userThreadsRepositoryCaseFailed = new UserThreadsRepository {

    override def save(userThreads: UserThreads): Future[Int] =
      Future.failed(UserThreadsRepositoryTestFailedException())

    override def findAllByUserId(userId: UserId): Future[Vector[UserThreads]] =
      Future.failed(UserThreadsRepositoryTestFailedException())
  }

  private def threadsRepositoryCaseSucceeded = new ThreadsRepository {

    private var database: Vector[models.Thread] = Vector.empty

    override def save(thread: models.Thread): Future[Int] =
      Future.successful {
        database = database :+ thread
        1
      }

    override def findById(threadId: ThreadId): Future[Option[models.Thread]] =
      Future.successful(database.find(_.id === threadId))

    override def findAllByUserId(userId: UserId): Future[Vector[models.Thread]] =
      Future.successful(database.filter(_.userId === userId))

    override def existsById(threadId: ThreadId): Future[Boolean] =
      Future.successful(database.exists(_.id === threadId))
  }

  private def threadsRepositoryCaseFailed = new ThreadsRepository {

    override def save(thread: models.Thread): Future[Int] =
      Future.failed(ThreadRepositoryTestFailedException())

    override def findById(threadId: ThreadId): Future[Option[models.Thread]] =
      Future.failed(ThreadRepositoryTestFailedException())

    override def findAllByUserId(userId: UserId): Future[Vector[models.Thread]] =
      Future.failed(ThreadRepositoryTestFailedException())

    override def existsById(threadId: ThreadId): Future[Boolean] =
      Future.failed(ThreadRepositoryTestFailedException())
  }

  "ThreadUseCase.generateRandomThreadId" should {

    "generate random thread ID of requested length" in {
      val threadUseCase =
        new ThreadUseCase(threadsRepositoryCaseSucceeded, userThreadsRepositoryCaseSucceeded)

      for {
        threadId0 <- threadUseCase.generateRandomThreadId(12)
        threadId1 <- threadUseCase.generateRandomThreadId(12)
      } yield assert {
        (threadId0 !== threadId1) &&
          (threadId0.value.length === 12 && threadId1.value.length === 12)
      }
    }

    "database error messages can be retrieved." in {
      val threadUseCase =
        new ThreadUseCase(threadsRepositoryCaseFailed, userThreadsRepositoryCaseFailed)

      recoverToSucceededIf[ThreadUseCaseTestException] {
        threadUseCase.generateRandomThreadId(12)
      }
    }
  }

  "ThreadUseCase.create" should {

    "returning random thread ID of length 12." in {
      val threadUseCase =
        new ThreadUseCase(threadsRepositoryCaseSucceeded, userThreadsRepositoryCaseSucceeded)

      val createCommand =
        ThreadUseCase.CreateCommand(
          userId = UserId(Utils.generateRandomString(12)),
          title = ThreadTitle(Utils.generateRandomString(12)),
          otherUserIds = List.empty)

      for {
        threadId0 <- threadUseCase.create(createCommand)
        threadId1 <- threadUseCase.create(createCommand)
      } yield {
        assert {
          (threadId0 !== threadId1) &&
            (threadId0.value.length === 12 && threadId1.value.length === 12)
        }
      }
    }

    "database error messages can be retrieved." in {
      val threadUseCase =
        new ThreadUseCase(threadsRepositoryCaseFailed, userThreadsRepositoryCaseFailed)

      val createCommand =
        ThreadUseCase.CreateCommand(
          userId = UserId(Utils.generateRandomString(12)),
          title = ThreadTitle(Utils.generateRandomString(8)),
          otherUserIds = List.empty)

      recoverToSucceededIf[ThreadUseCaseTestException] {
        threadUseCase.create(createCommand)
      }
    }

    "ThreadUseCase.findAllByUserId" should {

      "database error messages can be retrieved." in {
        val threadUseCase =
          new ThreadUseCase(threadsRepositoryCaseFailed, userThreadsRepositoryCaseFailed)

        recoverToSucceededIf[ThreadUseCaseTestException] {
          val userId = UserId(Utils.generateRandomString(12))
          threadUseCase.findAllByUserId(userId)
        }
      }
    }

    "ThreadUseCase" should {
      "can create and search messages." in {
        val threadUseCase =
          new ThreadUseCase(threadsRepositoryCaseSucceeded, userThreadsRepositoryCaseSucceeded)

        val userId = UserId(Utils.generateRandomString(12))
        val threadTitle = ThreadTitle(Utils.generateRandomString(8))

        val createCommand =
          ThreadUseCase.CreateCommand(
            userId = userId,
            title = threadTitle,
            otherUserIds = List.empty)

        for {
          _ <- {
            val task = threadUseCase.create(createCommand)
            Thread.sleep(300)
            task
          }
          thread <- threadUseCase.findAllByUserId(userId)
        } yield assert(thread.exists(_.title === threadTitle))
      }
    }
  }
}
