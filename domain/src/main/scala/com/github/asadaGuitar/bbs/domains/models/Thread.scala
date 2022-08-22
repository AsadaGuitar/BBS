package com.github.asadaGuitar.bbs.domains.models

import com.typesafe.config.ConfigFactory

import java.time.Instant
import scala.jdk.CollectionConverters.CollectionHasAsScala

final case class Thread(
    id: ThreadId,
    userId: UserId,
    title: ThreadTitle,
    isClose: Boolean = false,
    createAt: Instant = Instant.now(),
    modifyAt: Option[Instant] = None,
    closeAt: Option[Instant] = None
)

object ThreadId {

  private val lengthRequired =
    ConfigFactory.load().getIntList("application.domain.thread.id.lengths").asScala.toVector

  def matches(value: String): Boolean =
    lengthRequired.contains(value.length)

}

final case class ThreadId(value: String) {

  import ThreadId._

  require(matches(value))
}

object ThreadTitle {

  private val (minLengthRequired, maxLengthRequired) =
    (ConfigFactory.load().getIntList("application.domain.thread.title.length.min").asScala.min,
      ConfigFactory.load().getIntList("application.domain.thread.title.length.max").asScala.max)

  def matches(value: String): Boolean =
    minLengthRequired <= value.length && value.length <= maxLengthRequired
}

final case class ThreadTitle(value: String) {

  import ThreadTitle._

  require(matches(value))
}
