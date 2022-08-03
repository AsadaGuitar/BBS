package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao

private[adaptors] trait UserThreadsTable {

  self: Tables =>

  import profile.api._
  import slick.model.ForeignKeyAction
  import slick.jdbc.{ GetResult => GR }

  case class UserThreadsRow(
      id: Int,
      userId: String,
      threadId: String,
      isClose: Boolean = false,
      createAt: java.sql.Timestamp,
      closeAt: Option[java.sql.Timestamp] = None
  )

  implicit def GetResultUserThreadsRow(implicit
      e0: GR[Int],
      e1: GR[String],
      e2: GR[Boolean],
      e3: GR[java.sql.Timestamp],
      e4: GR[Option[java.sql.Timestamp]]
  ): GR[UserThreadsRow] = GR { prs =>
    import prs._
    UserThreadsRow.tupled(
      (<<[Int], <<[String], <<[String], <<[Boolean], <<[java.sql.Timestamp], <<?[java.sql.Timestamp])
    )
  }

  class UserThreads(_tableTag: Tag) extends profile.api.Table[UserThreadsRow](_tableTag, "user_threads") {
    def * = (id, userId, threadId, isClose, createAt, closeAt) <> (UserThreadsRow.tupled, UserThreadsRow.unapply)

    def ? =
      ((Rep.Some(id), Rep.Some(userId), Rep.Some(threadId), Rep.Some(isClose), Rep.Some(createAt), closeAt)).shaped.<>(
        { r => import r._; _1.map(_ => UserThreadsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6))) },
        (_: Any) => throw new Exception("Inserting into ? projection not supported.")
      )

    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

    val userId: Rep[String] = column[String]("user_id", O.Length(255, varying = true))

    val threadId: Rep[String] = column[String]("thread_id", O.Length(255, varying = true))

    val isClose: Rep[Boolean] = column[Boolean]("is_close", O.Default(false))

    val createAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_at")

    val closeAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("close_at", O.Default(None))

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

  lazy val UserThreads = new TableQuery(tag => new UserThreads(tag))
}
