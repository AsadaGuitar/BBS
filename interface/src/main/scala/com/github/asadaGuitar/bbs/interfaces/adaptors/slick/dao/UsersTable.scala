package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao
private[adaptors] trait UsersTable {

  self: Tables =>

  import profile.api._
  import slick.jdbc.{ GetResult => GR }

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

    val id: Rep[String] = column[String]("id", O.PrimaryKey, O.Length(255, varying = true))

    val firstName: Rep[String] = column[String]("first_name", O.Length(255, varying = true))

    val lastName: Rep[String] = column[String]("last_name", O.Length(255, varying = true))

    val emailAddress: Rep[String] = column[String]("email_address", O.Length(255, varying = true))

    val password: Rep[String] = column[String]("password", O.Length(255, varying = true))

    val isClose: Rep[Boolean] = column[Boolean]("is_close", O.Default(false))

    val createAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("create_at")

    val modifyAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("modify_at", O.Default(None))

    val closeAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("close_at", O.Default(None))
  }

  lazy val Users = new TableQuery(tag => new Users(tag))
}
