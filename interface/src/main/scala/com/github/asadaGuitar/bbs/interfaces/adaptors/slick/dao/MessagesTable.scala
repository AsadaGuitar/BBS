package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao
// AUTO-GENERATED Slick data model for table Messages
private[adaptors] trait MessagesTable {

  self: Tables =>

  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{ GetResult => GR }

  /** Entity class storing rows of table Messages
    * @param id
    *   Database column id SqlType(varchar), PrimaryKey, Length(255,true)
    * @param threadId
    *   Database column thread_id SqlType(varchar), Length(255,true)
    * @param userId
    *   Database column user_id SqlType(varchar), Length(255,true)
    * @param text
    *   Database column text SqlType(varchar), Length(255,true)
    * @param isClose
    *   Database column is_close SqlType(bool), Default(false)
    * @param createAt
    *   Database column create_at SqlType(timestamp)
    * @param modifyAt
    *   Database column modify_at SqlType(timestamp), Default(None)
    * @param closeAt
    *   Database column close_at SqlType(timestamp), Default(None)
    */
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

  /** GetResult implicit for fetching MessagesRow objects using plain SQL queries */
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

  /** Table description of table messages. Objects of this class serve as prototypes for rows in queries. */
  class Messages(_tableTag: Tag) extends profile.api.Table[MessagesRow](_tableTag, "messages") {
    def * =
      (id, threadId, userId, text, isClose, createAt, modifyAt, closeAt) <> (MessagesRow.tupled, MessagesRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
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

    /** Database column id SqlType(varchar), PrimaryKey, Length(255,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(255, varying = true))

    /** Database column thread_id SqlType(varchar), Length(255,true) */
    val threadId: Rep[String] = column[String]("thread_id", O.Length(255, varying = true))

    /** Database column user_id SqlType(varchar), Length(255,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(255, varying = true))

    /** Database column text SqlType(varchar), Length(255,true) */
    val text: Rep[String] = column[String]("text", O.Length(255, varying = true))

    /** Database column is_close SqlType(bool), Default(false) */
    val isClose: Rep[Boolean] = column[Boolean]("is_close", O.Default(false))

    /** Database column create_at SqlType(timestamp) */
    val createAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_at")

    /** Database column modify_at SqlType(timestamp), Default(None) */
    val modifyAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("modify_at", O.Default(None))

    /** Database column close_at SqlType(timestamp), Default(None) */
    val closeAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("close_at", O.Default(None))
  }

  /** Collection-like TableQuery object for table Messages */
  lazy val Messages = new TableQuery(tag => new Messages(tag))
}
