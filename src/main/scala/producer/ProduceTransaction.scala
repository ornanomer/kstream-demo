package producer

import java.io.InputStream
import java.nio.charset.CodingErrorAction
import java.util.Properties

import data.Transaction
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import serde.{TransactionSerde, TransactionSerializer}
import org.apache.poi.ss.usermodel.{DataFormatter, Row, WorkbookFactory}
import java.io.File

import org.apache.poi.xssf.usermodel.XSSFRow

import collection.JavaConversions._
import scala.io.{Codec, Source}
object ProduceTransaction {

  val kafkaProducerProps: Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", classOf[StringSerializer].getName)
    props.put("value.serializer", classOf[TransactionSerializer].getName)
    props
  }
val producer = new KafkaProducer[String, Transaction](kafkaProducerProps)




  def main(args: Array[String]): Unit = {
    readTransactionFileAndProduce("src/main/resources/bank.xlsx")
  }

  def readTransactionFileAndProduce(fileName : String) = {
    val f = new File(fileName)
    val workbook = WorkbookFactory.create(f)
    val sheet = workbook.getSheetAt(0) //
    for (row <- sheet) {
      if(row.getRowNum >0 ){
        print(row)
        createTran(row)
        produce(createTran(row), "beni")

      }
    }

  }

  def createTran(row : Row): Transaction ={
    val accountId = row.getCell(0).toString
    val withDraw = row.getCell(5).toString
    val deposit = row.getCell(6).toString
    val balance = row.getCell(7).toString
   Transaction(accountId, withDraw, deposit, balance)


  }


  def produce(tran: Transaction, topic : String): Unit ={
      producer.send(new ProducerRecord[String, Transaction](topic, "beni", tran))
    try Thread.sleep(50)
    catch {
      case e: InterruptedException =>
        throw new RuntimeException(e.getMessage)
    }

  }

}
