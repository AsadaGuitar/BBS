package com.github.asadaGuitar.bbs.interfaces.adaptors.slick.dao

import java.sql.Timestamp
import java.time.Instant

trait TablesSupporter {
  self: Tables =>

  import profile.api._

  implicit val instantColumnType: BaseColumnType[Instant] =
    MappedColumnType.base[Instant, Timestamp](
      instant => Timestamp.from(instant),
      ts => ts.toInstant
    )
}
