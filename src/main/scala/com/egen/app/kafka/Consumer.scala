package com.egen.app.kafka

import java.util
import java.util.Properties

import com.egen.app.kafka.Producer.getClass
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.slf4j.LoggerFactory

object Consumer extends App{
  val logger = LoggerFactory.getLogger(getClass)

  val kafkaStringSerializer = "org.apache.kafka.common.serialization.StringSerializer"
  val kafkaStringDeserializer = "org.apache.kafka.common.serialization.StringDeserializer"
  val topic = "events-topic"

  val props = new Properties
  props.put("bootstrap.servers", "localhost:9092")
  props.put("group.id", "test")
  props.put("enable.auto.commit", "true")
  props.put("auto.commit.interval.ms", "1000")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

  val consumer = new KafkaConsumer(props)
  consumer.subscribe(util.Arrays.asList(topic))

  while (true) {
    val records = consumer.poll(100)
    records.forEach(r => println(r.value()))
  }
}
