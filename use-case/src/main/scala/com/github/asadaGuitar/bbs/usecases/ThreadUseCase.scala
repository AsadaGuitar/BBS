package com.github.asadaGuitar.bbs.usecases

import cats.implicits.toTraverseOps
import com.github.asadaGuitar.bbs.domains.models.{Thread, ThreadId, ThreadTitle, UserId, UserThreads}
import com.github.asadaGuitar.bbs.domains.repositories.{ThreadsRepository, UserThreadsRepository}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object ThreadUseCase {
  final case class CreateCommand(userId: UserId, title: ThreadTitle, otherUserIds: List[UserId])
}

final class ThreadUseCase(threadsRepository: ThreadsRepository, userThreadsRepository: UserThreadsRepository)(implicit
    ec: ExecutionContext
) {
  import ThreadUseCase._

  private[usecases] def generateRandomThreadId(length: Int): Future[ThreadId] = {
    val threadId = ThreadId(Random.alphanumeric.take(length).mkString)
    threadsRepository.existsById(threadId).flatMap {
      case true  => generateRandomThreadId(length)
      case false => Future.successful(threadId)
    }
  }

  def create(createCommand: CreateCommand): Future[ThreadId] = {
    val CreateCommand(userId, title, otherUserIds) = createCommand
    for {
      threadId <- generateRandomThreadId(12)
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
      userThreadsVec <- userThreadsRepository.findAllByUserId(userId)
      threads <- userThreadsVec.map{ userThreads =>
        threadsRepository.findById(userThreads.threadId)
      }.sequence
    } yield threads.flatten.filter(!_.isClose)
}
