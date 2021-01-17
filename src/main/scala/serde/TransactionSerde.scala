package serde

import data.Transaction
import org.apache.kafka.common.serialization.{Deserializer, Serde, Serializer}

class TransactionSerde extends Serde[Transaction]{

  override def serializer(): Serializer[Transaction] = new TransactionSerializer

  override def deserializer(): Deserializer[Transaction] = new TransactionDeserializer
}
