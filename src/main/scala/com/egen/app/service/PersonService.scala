package com.egen.app.service

import com.egen.app.dao.PersonRepository
import com.egen.app.dto.PersonDto

class PersonService {

  def findOne(id: String): Option[PersonDto] = {
    PersonRepository.findOne(id)
  }

  def findAll(): List[PersonDto] = {
    PersonRepository.findAll
  }

  def create(person: PersonDto): Option[PersonDto] = {
    PersonRepository.create(person)
  }

  def delete(id:String): Option[PersonDto]= {
    PersonRepository.delete(id)
  }

  def update(id: String, personDto: PersonDto): Option[PersonDto] = {
    PersonRepository.update(id, personDto)
  }
}
