package com.egen.app.controller

import com.egen.app.EventsourcecoreStack
import com.egen.app.dto.EntityDto
import com.egen.app.service.EntityService
import org.scalatra.{NoContent, NotFound, Ok}


// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

// JSON handling support from Scalatra
import org.scalatra.json._


class EntityController extends EventsourcecoreStack with JacksonJsonSupport {
  val entityService = new EntityService

  before() {
    contentType = formats("json")
  }

  get("/find") {
    entityService.find
  }

  get("/entity/:id") {
    val id = params("id")
    entityService.findOne(id) match {
      case Some(entity) => Ok(entity)
      case None => NotFound("""{"message": "No entity found by given id"}""")
    }
  }

  get("/entity") {
    Ok(entityService.findAll)
  }

  post("/entity") {
    val entityDto = parsedBody.extract[EntityDto]
    entityService.create(entityDto)
    Ok("""{"message": "Created"}""")
  }

  put("/entity/:id") {
    val id = params("id")
    val entityDto = parsedBody.extract[EntityDto]
    id match {
      case entityDto.id =>
        entityService.update(id, entityDto) match {
          case Some(e) => Ok(e)
          case None => NotFound("""{"message": "No entity to update by given id"}""")
        }
      case _ => halt(status = 400, body = """{"message": "id does not match id in the body"}""")
    }
  }

  delete("/entity/:id") {
    val id = params("id")
    entityService.delete(id) match {
      case Some(e) => Ok(e)
      case None => NotFound("""{"message": "No entity found by given id"}""")
    }
  }

  protected implicit lazy val jsonFormats: Formats = DefaultFormats

}
