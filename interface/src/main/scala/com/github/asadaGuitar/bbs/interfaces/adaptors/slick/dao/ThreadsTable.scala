package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao

import java.sql.Timestamp
import java.util.Date

private[adaptors] trait ThreadsTable {

  self: Tables =>

  import profile.api._

  final case class ThreadsRow(
      id: String,
      userId: String,
      title: String,
      isClose: Boolean = false,
      createAt: java.sql.Timestamp = new Timestamp(new Date().getTime),
      modifyAt: Option[Timestamp] = None,
      closeAt: Option[Timestamp] = None
  )

  class Threads(_tableTag: Tag) extends profile.api.Table[ThreadsRow](_tableTag, "threads") {
    def * = (id, userId, title, isClose, createAt, modifyAt, closeAt) <> (ThreadsRow.tupled, ThreadsRow.unapply)

    val id = column[String]("id", O.PrimaryKey, O.Length(255, varying = true))

    val userId = column[String]("user_id", O.Length(255, varying = true))

    val title = column[String]("title", O.Length(255, varying = true))

    val isClose = column[Boolean]("is_close", O.Default(false))

    val createAt = column[java.sql.Timestamp]("create_at")

    val modifyAt = column[Option[java.sql.Timestamp]]("modify_at")

    val closeAt = column[Option[java.sql.Timestamp]]("close_at")

    lazy val usersFk = foreignKey("threads_user_id", userId, Users)(
      r => r.id,
      onUpdate = ForeignKeyAction.NoAction,
      onDelete = ForeignKeyAction.NoAction
    )
  }

  lazy val Threads = new TableQuery(new Threads(_))
}
