package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao

import java.sql.Timestamp
import java.util.Date

private[adaptors] trait UsersTable {

  self: Tables =>

  import profile.api._

  final case class UsersRow(
      id: String,
      firstName: String,
      lastName: String,
      emailAddress: String,
      password: String,
      isClose: Boolean = false,
      createAt: java.sql.Timestamp = new Timestamp(new Date().getTime),
      modifyAt: Option[java.sql.Timestamp] = None,
      closeAt: Option[java.sql.Timestamp] = None
  )

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

    val id = column[String]("id", O.PrimaryKey, O.Length(255, varying = true))

    val firstName = column[String]("first_name", O.Length(255, varying = true))

    val lastName = column[String]("last_name", O.Length(255, varying = true))

    val emailAddress = column[String]("email_address", O.Length(255, varying = true))

    val password = column[String]("password", O.Length(255, varying = true))

    val isClose = column[Boolean]("is_close", O.Default(false))

    val createAt = column[Timestamp]("create_at")

    val modifyAt = column[Option[Timestamp]]("modify_at", O.Default(None))

    val closeAt = column[Option[Timestamp]]("close_at", O.Default(None))
  }

  lazy val Users = new TableQuery(new Users(_))
}
