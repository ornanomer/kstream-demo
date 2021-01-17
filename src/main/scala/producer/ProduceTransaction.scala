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

import exercise.KafkaTopics
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
        createTran(row)
        produce(createTran(row), KafkaTopics.TRANSACTION_TOPIC)

      }
    }

  }

  def getNum(num : Double): Double =  num.toDouble

  def createTran(row : Row): Transaction ={
    val accountId = row.getCell(0).toString
    val withDraw = row.getCell(5).getNumericCellValue
    val deposit = row.getCell(6).getNumericCellValue
    val balance = row.getCell(7).getNumericCellValue
   Transaction(accountId, getNum(withDraw), getNum(deposit), getNum(balance))


  }


  def produce(tran: Transaction, topic : String): Unit ={
      producer.send(new ProducerRecord[String, Transaction](topic, "fakekey", tran))
    try Thread.sleep(50)
    catch {
      case e: InterruptedException =>
        throw new RuntimeException(e.getMessage)
    }

  }

}
