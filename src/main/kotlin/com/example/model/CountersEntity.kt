package com.example.model

import com.example.dto.response.CountersResponse
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object CountersEntity : IntIdTable() {
    val name = varchar("name", 255).uniqueIndex()
    val value = integer("value").default(0)
}

class CountersDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CountersDao>(CountersEntity)

    var name by CountersEntity.name
    var value by CountersEntity.value

    fun toResponse() = CountersResponse(name, value)
}