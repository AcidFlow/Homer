package info.acidflow.homer.model

import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo.{As, Id}
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}


@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "kind")
@JsonSubTypes(
  value = Array(
    new Type(value = classOf[SlotValueDuration], name = "Duration"),
    new Type(value = classOf[SlotValueCustom], name = "Custom")
  )
)
trait SlotValue


case class SlotValueDuration(
  years: Int,
  quarters: Int,
  months: Int,
  weeks: Int,
  days: Int,
  hours: Int,
  minutes: Int,
  seconds: Int,
  precision: String)
  extends SlotValue


case class SlotValueCustom(value: String) extends SlotValue