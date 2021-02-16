package exercise

import java.util.Properties

import data.Transaction
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.kstream.{Consumed, KStream}
import serde.TransactionSerde

object AggregateDrawPerAccountPer10Min {

  def main(args: Array[String]): Unit = {
    val config = new Properties
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "ks")
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[TransactionSerde])
    val builder = new StreamsBuilder



    val stream: KStream[String, Transaction] = builder.stream(KafkaTopics.TRANSACTION_TOPIC, Consumed.`with`(Serdes.String, new TransactionSerde))


    val streams = new KafkaStreams(builder.build, config)
    streams.start()

  }

}
