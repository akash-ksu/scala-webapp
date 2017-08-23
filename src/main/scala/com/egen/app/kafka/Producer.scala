package com.egen.app.kafka

import java.util.Properties

import com.egen.app.dto.EventDto
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.LoggerFactory
import org.json4s._
import org.json4s.jackson.Serialization.write

object Producer {

  implicit val formats = DefaultFormats

  val logger = LoggerFactory.getLogger(getClass)
  val kafkaStringSerializer = "org.apache.kafka.common.serialization.StringSerializer"
  val batchSize: java.lang.Integer = 64 * 1024
  val linger: java.lang.Integer = 5000
  val topic = "events-topic"


  val props = new Properties
  props.put("key.serializer", kafkaStringSerializer)
  props.put("value.serializer", kafkaStringSerializer)
  props.put("batch.size", batchSize)
  props.put("linger.ms", linger)
  props.put("bootstrap.servers", "localhost:9092")

  val producer = new KafkaProducer[String, String](props)

  def sendMessage(message: EventDto): Unit = {
    val record = new ProducerRecord[String, String](topic, message.id, write(message))
    producer.send(record)
  }

}
