package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao

private[adaptors] trait MessagesTable {

  self: Tables =>

  import profile.api._
  import slick.jdbc.{ GetResult => GR }

  case class MessagesRow(
      id: String,
      threadId: String,
      userId: String,
      text: String,
      isClose: Boolean = false,
      createAt: java.sql.Timestamp,
      modifyAt: Option[java.sql.Timestamp] = None,
      closeAt: Option[java.sql.Timestamp] = None
  )

  implicit def GetResultMessagesRow(implicit
      e0: GR[String],
      e1: GR[Boolean],
      e2: GR[java.sql.Timestamp],
      e3: GR[Option[java.sql.Timestamp]]
  ): GR[MessagesRow] = GR { prs =>
    import prs._
    MessagesRow.tupled(
      (
        <<[String],
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

  class Messages(_tableTag: Tag) extends profile.api.Table[MessagesRow](_tableTag, "messages") {
    def * =
      (id, threadId, userId, text, isClose, createAt, modifyAt, closeAt) <> (MessagesRow.tupled, MessagesRow.unapply)

    def ? = (
      (
        Rep.Some(id),
        Rep.Some(threadId),
        Rep.Some(userId),
        Rep.Some(text),
        Rep.Some(isClose),
        Rep.Some(createAt),
        modifyAt,
        closeAt
      )
    ).shaped.<>(
      { r => import r._; _1.map(_ => MessagesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7, _8))) },
      (_: Any) => throw new Exception("Inserting into ? projection not supported.")
    )

    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(255, varying = true))

    val threadId: Rep[String] = column[String]("thread_id", O.Length(255, varying = true))

    val userId: Rep[String] = column[String]("user_id", O.Length(255, varying = true))

    val text: Rep[String] = column[String]("text", O.Length(255, varying = true))

    val isClose: Rep[Boolean] = column[Boolean]("is_close", O.Default(false))

    val createAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_at")

    val modifyAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("modify_at", O.Default(None))

    val closeAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("close_at", O.Default(None))
  }

  lazy val Messages = new TableQuery(tag => new Messages(tag))
}
