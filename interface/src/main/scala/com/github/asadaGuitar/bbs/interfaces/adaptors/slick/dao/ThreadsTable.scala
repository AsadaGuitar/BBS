package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao

private[adaptors] trait ThreadsTable {

  self: Tables =>

  import profile.api._
  import slick.model.ForeignKeyAction
  import slick.jdbc.{ GetResult => GR }

  case class ThreadsRow(
      id: String,
      userId: String,
      title: String,
      isClose: Boolean = false,
      createAt: java.sql.Timestamp,
      modifyAt: Option[java.sql.Timestamp] = None,
      closeAt: Option[java.sql.Timestamp] = None
  )

  implicit def GetResultThreadsRow(implicit
      e0: GR[String],
      e1: GR[Boolean],
      e2: GR[java.sql.Timestamp],
      e3: GR[Option[java.sql.Timestamp]]
  ): GR[ThreadsRow] = GR { prs =>
    import prs._
    ThreadsRow.tupled(
      (
        <<[String],
        <<[String],
        <<[String],
        <<[Boolean],
        <<[java.sql.Timestamp],
        <<?[java.sql.Timestamp],
        <<?[java.sql.Timestamp]
      )
    )
  }

  class Threads(_tableTag: Tag) extends profile.api.Table[ThreadsRow](_tableTag, "threads") {
    def * = (id, userId, title, isClose, createAt, modifyAt, closeAt) <> (ThreadsRow.tupled, ThreadsRow.unapply)

    def ? = (
      (
        Rep.Some(id),
        Rep.Some(userId),
        Rep.Some(title),
        Rep.Some(isClose),
        Rep.Some(createAt),
        modifyAt,
        closeAt
      )
    ).shaped.<>(
      { r => import r._; _1.map(_ => ThreadsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7))) },
      (_: Any) => throw new Exception("Inserting into ? projection not supported.")
    )

    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(255, varying = true))

    val userId: Rep[String] = column[String]("user_id", O.Length(255, varying = true))

    val title: Rep[String] = column[String]("title", O.Length(255, varying = true))

    val isClose: Rep[Boolean] = column[Boolean]("is_close", O.Default(false))

    val createAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_at")

    val modifyAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("modify_at", O.Default(None))

    val closeAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("close_at", O.Default(None))

    lazy val usersFk = foreignKey("threads_user_id", userId, Users)(
      r => r.id,
      onUpdate = ForeignKeyAction.NoAction,
      onDelete = ForeignKeyAction.NoAction
    )
  }

  lazy val Threads = new TableQuery(tag => new Threads(tag))
}
