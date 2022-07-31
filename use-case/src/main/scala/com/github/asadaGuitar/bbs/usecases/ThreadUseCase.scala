package com.github.asadaGuitar.bbs.usecases

import cats.implicits.toTraverseOps
import com.github.asadaGuitar.bbs.domains.models.{Thread, ThreadId, UserId}
import com.github.asadaGuitar.bbs.repositories.{ThreadsRepository, UserThreadsRepository}
import com.github.asadaGuitar.bbs.repositories.models.{ThreadForm, UserThreadsForm}
import com.github.asadaGuitar.bbs.usecases.models.PostThreadForm

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

final class ThreadUseCase(threadsRepository: ThreadsRepository,
                          userThreadsRepository: UserThreadsRepository)
                         (implicit ec: ExecutionContext) {

  private def generateRandomThreadId(randomString: String = Random.alphanumeric.take(12).mkString): Future[ThreadId] ={
    val threadId = ThreadId(randomString)
    threadsRepository.existsById(threadId).flatMap{
      case true  => generateRandomThreadId()
      case false => Future.successful(threadId)
    }
  }

  def create(postThreadForm: PostThreadForm): Future[ThreadId] ={
    postThreadForm match {
      case PostThreadForm(userId, title, otherUserIds) =>
        for {
          threadId <- generateRandomThreadId()
          threadForm = ThreadForm(threadId, userId, title)
          _ <- threadsRepository.save(threadForm)
          _ <- otherUserIds.map{ otherUserId =>
            val userThreadsForm = UserThreadsForm(otherUserId, threadId)
            userThreadsRepository.save(userThreadsForm)
          }.sequence
        } yield threadId
    }
  }

  def findAllByUserId(userId: UserId): Future[List[Thread]] =
    for {
      threadIds <- userThreadsRepository.findAllByUserId(userId)
      threads <- threadIds.map(threadsRepository.findById).sequence
    } yield threads.flatten

}
