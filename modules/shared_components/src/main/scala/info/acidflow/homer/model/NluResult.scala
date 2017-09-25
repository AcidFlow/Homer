package info.acidflow.homer.model

case class NluResult(input : String, intent : IntentInfo, slots : Array[Slot]) {

  final def extractSlotMap(): Map[String, Slot] = {
    slots.groupBy(s => s.slotName).mapValues(v => v.head)
  }
}