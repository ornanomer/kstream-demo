package exercise

import java.util.Properties

import data.Transaction
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import serde.TransactionSerde
import org.apache.kafka.streams.kstream.Materialized
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes.String
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}

object GetMaxDrawPerAccount {

  def main(args: Array[String]): Unit = {
    implicit val userClicksSerde: Serde[Transaction] = new TransactionSerde

    val config = new Properties
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "ks")
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[TransactionSerde])
    val builder = new StreamsBuilder



    val stream: KStream[String, Transaction] = builder.stream[String, Transaction](KafkaTopics.TRANSACTION_TOPIC)



    val streams = new KafkaStreams(builder.build, config)
    streams.start()
  }

}
