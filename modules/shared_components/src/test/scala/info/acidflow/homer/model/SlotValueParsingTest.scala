package info.acidflow.homer.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.scalatest.FunSuite

class SlotValueParsingTest extends FunSuite {

  test("Deserialization SlotValue should succeed") {
    val jsonString = """{"kind":"Duration","years":0,"quarters":0,"months":0,"weeks":0,"days":0,"hours":0,"minutes":0,"seconds":10,"precision":"Exact"}"""
    val mapper = new ObjectMapper().registerModule(DefaultScalaModule)
    mapper.readValue(jsonString, classOf[SlotValue])
  }

  test("Deserialization Nlu should succeed"){
    val jsonString = """{"input":"set the timer for ten seconds","intent":{"intentName":"user_mZkrZwb4d__StartTimer","probability":0.56699616},"slots":[{"rawValue":"for ten seconds","value":{"kind":"Duration","years":0,"quarters":0,"months":0,"weeks":0,"days":0,"hours":0,"minutes":0,"seconds":10,"precision":"Exact"},"range":{"start":14,"end":29},"entity":"snips/duration","slotName":"duration"},{"rawValue":"cooking besta","value":{"kind":"Custom","value":"cooking besta"},"range":null,"entity":"snips/default--timerName","slotName":"timerName"}]}"""
    val mapper = new ObjectMapper().registerModule(DefaultScalaModule)
    mapper.readValue(jsonString, classOf[NluResult])
  }
}
