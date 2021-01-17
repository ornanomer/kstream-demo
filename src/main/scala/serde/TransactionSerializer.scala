package serde

import com.fasterxml.jackson.databind.ObjectMapper
import data.Transaction
import org.apache.kafka.common.serialization.Serializer
import org.slf4j.LoggerFactory

class TransactionSerializer extends Serializer[Transaction]{
  val log = LoggerFactory.getLogger(classOf[TransactionSerializer]);
  val objectMapper = new ObjectMapper

  override def serialize(s: String, t: Transaction): Array[Byte] = {
    try{
      return objectMapper.writeValueAsString(t).getBytes
    }
    catch {
      case e: Exception =>
        log.error("failed to serialize child", e)
        throw new RuntimeException(e)
    }
    null
  }

}
