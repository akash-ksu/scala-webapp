package com.egen.app.controller

import java.util.UUID

import com.egen.app.EventsourcecoreStack
import com.egen.app.dto.{EventDto, PersonDto}
import com.egen.app.kafka.Producer
import com.egen.app.service.PersonService
import org.scalatra.{NotFound, Ok}
import org.json4s._
import org.json4s.jackson.Serialization.write


// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

// JSON handling support from Scalatra
import org.scalatra.json._


class PersonController extends EventsourcecoreStack with JacksonJsonSupport {
  val personService = new PersonService

  before() {
    contentType = formats("json")
  }

  get("/person/:id") {
    val id = params("id")
    personService.findOne(id) match {
      case Some(person) => Ok(person)
      case None => NotFound("""{"message": "No person found by given id"}""")
    }
  }

  get("/person") {
    Ok(personService.findAll)
  }

  post("/person") {
    val personDto = parsedBody.extract[PersonDto]
    personService.create(personDto) match {
      case Some(p) =>
        Producer.sendMessage(new EventDto(UUID.randomUUID().toString, "Create", p.id, write(p)))
        Ok(p)
      case None => halt(status = 400, body = """{"message": "person already exists"}""")
    }
  }

  put("/person/:id") {
    val id = params("id")
    val personDto = parsedBody.extract[PersonDto]
    id match {
      case personDto.id =>
        personService.update(id, personDto) match {
          case Some(p) =>
            Producer.sendMessage(new EventDto(UUID.randomUUID().toString, "Update", p.id, write(p)))
            Ok(p)
          case None => NotFound("""{"message": "No person to update by given id"}""")
        }
      case _ => halt(status = 400, body = """{"message": "id does not match id in the person body"}""")
    }
  }

  delete("/person/:id") {
    val id = params("id")
    personService.delete(id) match {
      case Some(p) =>
        Producer.sendMessage(new EventDto(UUID.randomUUID().toString, "Delete", p.id, write(p)))
        Ok(p)
      case None => NotFound("""{"message": "No person found by given id"}""")
    }
  }

  protected implicit lazy val jsonFormats: Formats = DefaultFormats

}
