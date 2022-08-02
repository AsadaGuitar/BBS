package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao
// AUTO-GENERATED Slick data model for table Users
private[adaptors] trait UsersTable {

  self: Tables =>

  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{ GetResult => GR }

  /** Entity class storing rows of table Users
    * @param id
    *   Database column id SqlType(varchar), PrimaryKey, Length(255,true)
    * @param firstName
    *   Database column first_name SqlType(varchar), Length(255,true)
    * @param lastName
    *   Database column last_name SqlType(varchar), Length(255,true)
    * @param emailAddress
    *   Database column email_address SqlType(varchar), Length(255,true)
    * @param password
    *   Database column password SqlType(varchar), Length(255,true)
    * @param isClose
    *   Database column is_close SqlType(bool), Default(false)
    * @param createAt
    *   Database column create_at SqlType(timestamp)
    * @param modifyAt
    *   Database column modify_at SqlType(timestamp), Default(None)
    * @param closeAt
    *   Database column close_at SqlType(timestamp), Default(None)
    */
  case class UsersRow(
      id: String,
      firstName: String,
      lastName: String,
      emailAddress: String,
      password: String,
      isClose: Boolean = false,
      createAt: java.sql.Timestamp,
      modifyAt: Option[java.sql.Timestamp] = None,
      closeAt: Option[java.sql.Timestamp] = None
  )

  /** GetResult implicit for fetching UsersRow objects using plain SQL queries */
  implicit def GetResultUsersRow(implicit
      e0: GR[String],
      e1: GR[Boolean],
      e2: GR[java.sql.Timestamp],
      e3: GR[Option[java.sql.Timestamp]]
  ): GR[UsersRow] = GR { prs =>
    import prs._
    UsersRow.tupled(
      (
        <<[String],
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

  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends profile.api.Table[UsersRow](_tableTag, "users") {
    def * = (
      id,
      firstName,
      lastName,
      emailAddress,
      password,
      isClose,
      createAt,
      modifyAt,
      closeAt
    ) <> (UsersRow.tupled, UsersRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (
      (
        Rep.Some(id),
        Rep.Some(firstName),
        Rep.Some(lastName),
        Rep.Some(emailAddress),
        Rep.Some(password),
        Rep.Some(isClose),
        Rep.Some(createAt),
        modifyAt,
        closeAt
      )
    ).shaped.<>(
      { r =>
        import r._; _1.map(_ => UsersRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8, _9)))
      },
      (_: Any) => throw new Exception("Inserting into ? projection not supported.")
    )

    /** Database column id SqlType(varchar), PrimaryKey, Length(255,true) */
    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(255, varying = true))

    /** Database column first_name SqlType(varchar), Length(255,true) */
    val firstName: Rep[String] = column[String]("first_name", O.Length(255, varying = true))

    /** Database column last_name SqlType(varchar), Length(255,true) */
    val lastName: Rep[String] = column[String]("last_name", O.Length(255, varying = true))

    /** Database column email_address SqlType(varchar), Length(255,true) */
    val emailAddress: Rep[String] = column[String]("email_address", O.Length(255, varying = true))

    /** Database column password SqlType(varchar), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255, varying = true))

    /** Database column is_close SqlType(bool), Default(false) */
    val isClose: Rep[Boolean] = column[Boolean]("is_close", O.Default(false))

    /** Database column create_at SqlType(timestamp) */
    val createAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_at")

    /** Database column modify_at SqlType(timestamp), Default(None) */
    val modifyAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("modify_at", O.Default(None))

    /** Database column close_at SqlType(timestamp), Default(None) */
    val closeAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("close_at", O.Default(None))
  }

  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
