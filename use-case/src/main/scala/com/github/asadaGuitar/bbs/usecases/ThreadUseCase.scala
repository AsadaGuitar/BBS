package com.github.asadaGuitar.bbs.usecases

import cats.implicits.toTraverseOps
import com.github.asadaGuitar.bbs.domains.models.{ Thread, ThreadId, ThreadTitle, UserId, UserThreads }
import com.github.asadaGuitar.bbs.repositories.{ ThreadsRepository, UserThreadsRepository }

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Random

object ThreadUseCase {
  final case class CreateCommand(userId: UserId, title: ThreadTitle, otherUserIds: List[UserId])
}

final class ThreadUseCase(threadsRepository: ThreadsRepository, userThreadsRepository: UserThreadsRepository)(implicit
    ec: ExecutionContext
) {
  import ThreadUseCase._

  private def generateRandomThreadId(randomString: String = Random.alphanumeric.take(12).mkString): Future[ThreadId] = {
    val threadId = ThreadId(randomString)
    threadsRepository.existsById(threadId).flatMap {
      case true  => generateRandomThreadId()
      case false => Future.successful(threadId)
    }
  }

  def create(createCommand: CreateCommand): Future[ThreadId] = {
    val CreateCommand(userId, title, otherUserIds) = createCommand
    for {
      threadId <- generateRandomThreadId()
      thread = Thread(id = threadId, userId = userId, title = title)
      _ <- threadsRepository.save(thread)
      _ <- (userId +: otherUserIds).map { otherUserId =>
        val userThreads = UserThreads(userId = otherUserId, threadId = threadId)
        userThreadsRepository.save(userThreads)
      }.sequence
    } yield threadId
  }

  def findAllByUserId(userId: UserId): Future[Vector[Thread]] =
    for {
      threadIds <- userThreadsRepository.findAllByUserId(userId)
      threads <- threadIds.map(threadsRepository.findById).sequence
    } yield threads.flatten
}
