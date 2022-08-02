package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao
import slick.lifted.ForeignKeyQuery
// AUTO-GENERATED Slick data model for table UserThreads
private[adaptors] trait UserThreadsTable {

  self: Tables =>

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{ GetResult => GR }

  /** Entity class storing rows of table UserThreads
    * @param id
    *   Database column id SqlType(serial), AutoInc, PrimaryKey
    * @param userId
    *   Database column user_id SqlType(varchar), Length(255,true)
    * @param threadId
    *   Database column thread_id SqlType(varchar), Length(255,true)
    * @param isClose
    *   Database column is_close SqlType(bool), Default(false)
    * @param createAt
    *   Database column create_at SqlType(timestamp)
    * @param closeAt
    *   Database column close_at SqlType(timestamp), Default(None)
    */
  case class UserThreadsRow(
      id: Int,
      userId: String,
      threadId: String,
      isClose: Boolean = false,
      createAt: java.sql.Timestamp,
      closeAt: Option[java.sql.Timestamp] = None
  )

  /** GetResult implicit for fetching UserThreadsRow objects using plain SQL queries */
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

  /** Table description of table user_threads. Objects of this class serve as prototypes for rows in queries. */
  class UserThreads(_tableTag: Tag) extends profile.api.Table[UserThreadsRow](_tableTag, "user_threads") {
    def * = (id, userId, threadId, isClose, createAt, closeAt) <> (UserThreadsRow.tupled, UserThreadsRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? =
      ((Rep.Some(id), Rep.Some(userId), Rep.Some(threadId), Rep.Some(isClose), Rep.Some(createAt), closeAt)).shaped.<>(
        { r => import r._; _1.map(_ => UserThreadsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6))) },
        (_: Any) => throw new Exception("Inserting into ? projection not supported.")
      )

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

    /** Database column user_id SqlType(varchar), Length(255,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(255, varying = true))

    /** Database column thread_id SqlType(varchar), Length(255,true) */
    val threadId: Rep[String] = column[String]("thread_id", O.Length(255, varying = true))

    /** Database column is_close SqlType(bool), Default(false) */
    val isClose: Rep[Boolean] = column[Boolean]("is_close", O.Default(false))

    /** Database column create_at SqlType(timestamp) */
    val createAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_at")

    /** Database column close_at SqlType(timestamp), Default(None) */
    val closeAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("close_at", O.Default(None))

    /** Foreign key referencing Threads (database name user_threads_threads_id) */
    lazy val threadsFk: ForeignKeyQuery[Threads, ThreadsRow] = foreignKey("user_threads_threads_id", threadId, Threads)(
      r => r.id,
      onUpdate = ForeignKeyAction.NoAction,
      onDelete = ForeignKeyAction.NoAction
    )

    /** Foreign key referencing Users (database name user_threads_user_id) */
    lazy val usersFk: ForeignKeyQuery[Users, UsersRow] = foreignKey("user_threads_user_id", userId, Users)(
      r => r.id,
      onUpdate = ForeignKeyAction.NoAction,
      onDelete = ForeignKeyAction.NoAction
    )
  }

  /** Collection-like TableQuery object for table UserThreads */
  lazy val UserThreads = new TableQuery(tag => new UserThreads(tag))
}
