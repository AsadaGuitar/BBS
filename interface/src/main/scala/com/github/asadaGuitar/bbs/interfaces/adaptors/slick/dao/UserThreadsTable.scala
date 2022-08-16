package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao

import java.sql.Timestamp
import java.util.Date

private[adaptors] trait UserThreadsTable {

  self: Tables =>

  import profile.api._
  import slick.model.ForeignKeyAction

  final case class UserThreadsRow(
      id: Int,
      userId: String,
      threadId: String,
      isClose: Boolean = false,
      createAt: java.sql.Timestamp = new Timestamp(new Date().getTime),
      closeAt: Option[java.sql.Timestamp] = None
  )

  class UserThreads(_tableTag: Tag) extends profile.api.Table[UserThreadsRow](_tableTag, "user_threads") {
    def * = (id, userId, threadId, isClose, createAt, closeAt) <> (UserThreadsRow.tupled, UserThreadsRow.unapply)

    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)

    val userId = column[String]("user_id", O.Length(255, varying = true))

    val threadId = column[String]("thread_id", O.Length(255, varying = true))

    val isClose = column[Boolean]("is_close", O.Default(false))

    val createAt = column[Timestamp]("create_at")

    val closeAt = column[Option[Timestamp]]("close_at", O.Default(None))

    lazy val threadsFk = foreignKey("user_threads_threads_id", threadId, Threads)(
      r => r.id,
      onUpdate = ForeignKeyAction.NoAction,
      onDelete = ForeignKeyAction.NoAction
    )

    lazy val usersFk = foreignKey("user_threads_user_id", userId, Users)(
      r => r.id,
      onUpdate = ForeignKeyAction.NoAction,
      onDelete = ForeignKeyAction.NoAction
    )
  }

  lazy val UserThreads = new TableQuery(new UserThreads(_))
}
