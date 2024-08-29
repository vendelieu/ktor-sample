package com.example.service

import com.example.model.CountersDao
import com.example.model.CountersEntity
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

object CountersService {
    fun get(name: String): CountersDao? = CountersDao.find {
        CountersEntity.name eq name
    }.singleOrNull()

    fun create(name: String, value: Int): CountersDao = CountersDao.new {
        this.name = name
        this.value = value
    }

    fun increment(name: String): Int? = CountersDao.findSingleByAndUpdate(CountersEntity.name eq name) {
        it.value += 1
    }?.value

    fun delete(name: String): Int = CountersEntity.deleteWhere { CountersEntity.name eq name }

    fun getAll() = CountersDao.all()
}