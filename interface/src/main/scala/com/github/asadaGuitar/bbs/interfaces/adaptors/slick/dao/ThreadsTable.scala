package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao
// AUTO-GENERATED Slick data model for table Threads
private[adaptors] trait ThreadsTable {

  self:Tables  =>

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table Threads
   *  @param id Database column id SqlType(varchar), PrimaryKey, Length(255,true)
   *  @param userId Database column user_id SqlType(varchar), Length(255,true)
   *  @param title Database column title SqlType(varchar), Length(255,true)
   *  @param isClose Database column is_close SqlType(bool), Default(false)
   *  @param createAt Database column create_at SqlType(timestamp)
   *  @param modifyAt Database column modify_at SqlType(timestamp), Default(None)
   *  @param closeAt Database column close_at SqlType(timestamp), Default(None) */
  case class ThreadsRow(id: String, userId: String, title: String, isClose: Boolean = false, createAt: java.sql.Timestamp, modifyAt: Option[java.sql.Timestamp] = None, closeAt: Option[java.sql.Timestamp] = None)
  /** GetResult implicit for fetching ThreadsRow objects using plain SQL queries */
  implicit def GetResultThreadsRow(implicit e0: GR[String], e1: GR[Boolean], e2: GR[java.sql.Timestamp], e3: GR[Option[java.sql.Timestamp]]): GR[ThreadsRow] = GR{
    prs => import prs._
    ThreadsRow.tupled((<<[String], <<[String], <<[String], <<[Boolean], <<[java.sql.Timestamp], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp]))
  }
  /** Table description of table threads. Objects of this class serve as prototypes for rows in queries. */
  class Threads(_tableTag: Tag) extends profile.api.Table[ThreadsRow](_tableTag, "threads") {
    def * = (id, userId, title, isClose, createAt, modifyAt, closeAt) <> (ThreadsRow.tupled, ThreadsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(userId), Rep.Some(title), Rep.Some(isClose), Rep.Some(createAt), modifyAt, closeAt)).shaped.<>({r=>import r._; _1.map(_=> ThreadsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(varchar), PrimaryKey, Length(255,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(255,varying=true))
    /** Database column user_id SqlType(varchar), Length(255,true) */
    val userId: Rep[String] = column[String]("user_id", O.Length(255,varying=true))
    /** Database column title SqlType(varchar), Length(255,true) */
    val title: Rep[String] = column[String]("title", O.Length(255,varying=true))
    /** Database column is_close SqlType(bool), Default(false) */
    val isClose: Rep[Boolean] = column[Boolean]("is_close", O.Default(false))
    /** Database column create_at SqlType(timestamp) */
    val createAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_at")
    /** Database column modify_at SqlType(timestamp), Default(None) */
    val modifyAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("modify_at", O.Default(None))
    /** Database column close_at SqlType(timestamp), Default(None) */
    val closeAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("close_at", O.Default(None))

    /** Foreign key referencing Users (database name threads_user_id) */
    lazy val usersFk = foreignKey("threads_user_id", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Threads */
  lazy val Threads = new TableQuery(tag => new Threads(tag))
}
