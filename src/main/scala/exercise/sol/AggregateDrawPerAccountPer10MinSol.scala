package exercise.sol

import java.util.Properties
import java.util.concurrent.TimeUnit

import data.Transaction
import exercise.KafkaTopics
import exercise.KafkaTopics.WINDOW_SUM_TRAN
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import serde.TransactionSerde
import org.apache.kafka.streams.kstream.{Materialized, TimeWindows, Windowed}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.Serdes.{Double, Integer, String}
import org.apache.kafka.streams.scala._
import org.apache.kafka.streams.scala.kstream._
import org.apache.kafka.streams.{KafkaStreams, StreamsConfig}


object AggregateDrawPerAccountPer10MinSol {

  def main(args: Array[String]): Unit = {
    implicit val transactionSerde: Serde[Transaction] = new TransactionSerde

    val config = new Properties
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "ks") //consumer group
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092") // kafka broker
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[TransactionSerde])
    val builder = new StreamsBuilder




    val stream: KStream[Windowed[String], Double] = builder.
      stream[String, Transaction](KafkaTopics.TRANSACTION_TOPIC).map[String, Double]((k,v) => (v.accountId, v.withDraw))
      .groupByKey.windowedBy(TimeWindows.of(TimeUnit.SECONDS.toMillis( 5 ) )).reduce((v1,v2) => v1 +v2).toStream

    stream.foreach((k,v) => print( "start -> " + k.window().start() + "  key -> " + k.key() + " value ->" + v.toString() ))

    val streams = new KafkaStreams(builder.build, config)
    streams.start()

  }

}
