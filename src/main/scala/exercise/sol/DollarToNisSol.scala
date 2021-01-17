package exercise.sol

import java.util.Properties

import data.Transaction
import exercise.KafkaTopics
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
object DollarToNisSol {
  val NIS_TO_DOLLAR = 3

  def dollarToNis(tran : Transaction) : Transaction ={
      Transaction(tran.accountId, tran.withDraw * NIS_TO_DOLLAR,
        tran.withDraw * NIS_TO_DOLLAR, tran.balance * NIS_TO_DOLLAR)
  }

  def main(args: Array[String]): Unit = {
    implicit val transactionSerde: Serde[Transaction] = new TransactionSerde

    val config = new Properties
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "ks") //consumer group
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092") // kafka broker
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass.getName)
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, classOf[TransactionSerde])
    val builder = new StreamsBuilder



    val stream: KStream[String, Transaction] = builder.
      stream[String, Transaction](KafkaTopics.TRANSACTION_TOPIC)


    stream.mapValues(tran => dollarToNis(tran))
      .to(KafkaTopics.NIS_TRANSACTION)
    val streams = new KafkaStreams(builder.build, config)

    //start the stream
    streams.start()
  }


}
