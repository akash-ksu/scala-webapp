package com.egen.app.dao

import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.slf4j.LoggerFactory
import java.net.InetAddress

import com.egen.app.dto.PersonDto
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.update.UpdateRequest
import org.json4s._
import org.json4s.jackson.Serialization.write
import org.json4s.jackson.Serialization.read
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.index.query.QueryBuilders

object PersonRepository {

  implicit val formats = DefaultFormats

  val loggerFactory = LoggerFactory.getLogger(getClass)

  val clientSettings = Settings.settingsBuilder.put("cluster.name", "elasticsearch").build

  val address = new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300)

  val client = TransportClient.builder.settings(clientSettings).build().addTransportAddress(address)

  val index = "person_index"

  val doc_type = "person"

  def findAll(): List[PersonDto] = {
    val response = client.prepareSearch(index).setTypes(doc_type).execute().actionGet()
    val output = response.getHits.getHits.toStream.map(s => read[PersonDto](s.getSourceAsString)).toList

    output
  }

  def findOne(id: String): Option[PersonDto] = {
    val matchId = QueryBuilders.matchQuery("id", id)
    val response = client.prepareSearch(index).setTypes(doc_type).setQuery(matchId).setSize(1).execute().actionGet()
    val output = response.getHits.getHits.toStream.map(s => read[PersonDto](s.getSourceAsString)).toList.headOption

    output
  }

  def create(p: PersonDto): Option[PersonDto] = {
    findOne(p.id) match {
      case Some(p) => None
      case None =>
        val indexRequest = new IndexRequest(index, doc_type, p.id).source(write(p))
        val updateRequest = new UpdateRequest(index, doc_type, p.id).doc(write(p)).upsert(indexRequest)
        client.update(updateRequest).get
        Some(p)
    }
  }

  def update(id: String, person: PersonDto): Option[PersonDto] = {
    findOne(person.id) match {
      case Some(p) =>
        val indexRequest = new IndexRequest(index, doc_type, p.id).source(write(person))
        val updateRequest = new UpdateRequest(index, doc_type, p.id).doc(write(person)).upsert(indexRequest)
        client.update(updateRequest).get
        Some(person)
      case None => None
    }
  }

  def delete(id: String): Option[PersonDto] = {
    findOne(id) match {
      case Some(p) =>
        client.prepareDelete(index, doc_type, id).get()
        Some(p)
      case None => None
    }
  }
}
