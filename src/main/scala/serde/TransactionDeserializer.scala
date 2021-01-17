package serde

import org.apache.kafka.common.serialization.Deserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import data.Transaction
import org.slf4j.LoggerFactory

class TransactionDeserializer extends Deserializer[Transaction]{
  val log = LoggerFactory.getLogger(classOf[TransactionDeserializer]);
  val objectMapper = new ObjectMapper() with ScalaObjectMapper

  objectMapper.registerModule(DefaultScalaModule)
  override def deserialize(s: String, bytes: Array[Byte]): Transaction = {
    try{
      if(bytes != null){
        return objectMapper.readValue(bytes, classOf[Transaction])
      }
    }
    catch {
    case e: Exception =>
      log.error("failed to deserialize ", e)
      throw new RuntimeException(e)
  }
    return null;

  }
}
