package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao

import slick.sql.SqlProfile.ColumnOption.SqlType

import java.sql.Timestamp
import java.util.Date

private[adaptors] trait MessagesTable {

  self: Tables =>

  import profile.api._

  final case class MessagesRow(
      id: String,
      threadId: String,
      userId: String,
      text: String,
      isClose: Boolean = false,
      createAt: java.sql.Timestamp = new Timestamp(new Date().getTime),
      modifyAt: Option[java.sql.Timestamp] = None,
      closeAt: Option[java.sql.Timestamp] = None
  )

  class Messages(_tableTag: Tag) extends profile.api.Table[MessagesRow](_tableTag, "messages") {
    def * =
      (id, threadId, userId, text, isClose, createAt, modifyAt, closeAt) <> (MessagesRow.tupled, MessagesRow.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(255, varying = true))

    val threadId = column[String]("thread_id", O.Length(255, varying = true))

    val userId = column[String]("user_id", O.Length(255, varying = true))

    val text = column[String]("text", O.Length(255, varying = true))

    val isClose = column[Boolean]("is_close", O.Default(false))

    val createAt = column[Timestamp]("create_at", SqlType("timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL"))

    val modifyAt = column[Option[Timestamp]]("modify_at")

    val closeAt = column[Option[Timestamp]]("close_at")
  }

  lazy val Messages = new TableQuery(new Messages(_))
}
