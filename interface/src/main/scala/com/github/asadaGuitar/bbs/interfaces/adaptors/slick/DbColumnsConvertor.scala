package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import java.sql.Timestamp
import java.time.Instant

trait DbColumnsConvertor {

  implicit val convertTimestampToInstant: Timestamp => Instant =
    timestamp => timestamp.toInstant

  implicit val convertInstantToTimestamp: Instant => Timestamp =
    instant => Timestamp.from(instant)

  implicit val convertTimestampToInstantOption: Option[Timestamp] => Option[Instant] =
    timestamp => timestamp.map(_.toInstant)

  implicit val convertInstantToTimestampOption: Option[Instant] => Option[Timestamp] =
    instant => instant.map(Timestamp.from)
}
