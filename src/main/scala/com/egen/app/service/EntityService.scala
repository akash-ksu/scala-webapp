package com.egen.app.service

import com.egen.app.dao.EntityRepository
import com.egen.app.dto.EntityDto

class EntityService {
  val entityRepo = new EntityRepository

  def find() = {
    entityRepo.find
  }

  def findOne(id: String): Option[EntityDto] = {
    EntityData.all.filter(e => e.id == id).headOption
  }

  def findAll(): List[EntityDto] = {
    EntityData.all
  }

  def create(entity: EntityDto): Unit = {
    EntityData.all = entity::EntityData.all
  }

  def delete(id:String): Option[EntityDto]= {
    findOne(id) match {
      case Some(e) =>
        EntityData.all = EntityData.all.filter(e => e.id != id)
        Some(e)
      case None => None
    }
  }

  def update(id: String, entityDto: EntityDto): Option[EntityDto] = {
    findOne(id) match {
      case Some(e) =>
        delete(id)
        create(entityDto)
        Some(entityDto)
      case None => None
    }
  }

  object EntityData {
    var all = List(
      EntityDto("1", "akash's account"),
      EntityDto("2", "pankaj's account"),
      EntityDto("3", "himanshu's account")
    )
  }
}
