package com.github.asadaGuitar.bbs.interfaces.adaptors.slick

import java.sql.Timestamp
import java.time.Instant

trait DbColumnsConvertor {

  implicit val convertTimestampToInstant: Timestamp => Instant =
    timestamp => Instant.ofEpochMilli(timestamp.getTime)

  implicit val convertInstantToTimestamp: Instant => Timestamp =
    instant => new Timestamp(instant.toEpochMilli)

  implicit val convertTimestampToInstantOption: Option[Timestamp] => Option[Instant] =
    timestamp => timestamp.map(ts => Instant.ofEpochMilli(ts.getTime))

  implicit val convertInstantToTimestampOption: Option[Instant] => Option[Timestamp] =
    instant => instant.map(i => new Timestamp(i.toEpochMilli))
}
