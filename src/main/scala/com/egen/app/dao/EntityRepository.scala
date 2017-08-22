package com.egen.app.dao

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.{ElasticClient, ElasticsearchClientUri, TcpClient}
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy
import scaldi.{Injectable, Injector}
import Injectable._
import org.slf4j.LoggerFactory

object EntityRepository {

  val loggerFactory = LoggerFactory
//  def find() = {
//    val client = inject [ElasticClient]
//    client.execute {
//      indexInto("bands" / "artists") fields ("name" -> "coldplay") refresh(RefreshPolicy.IMMEDIATE)
//    }.await
//
//    val resp = client.execute {
//      search("bands" / "artists") query "coldplay"
//    }.await
//
//    resp
//  }
}
