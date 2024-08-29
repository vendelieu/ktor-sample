package com.example

import com.example.configuration.ExceptionHandlerConfigurator
import com.example.configuration.ValidatorConfigurator
import com.example.model.CountersEntity
import com.example.routes.configureRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.main() {
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages, ExceptionHandlerConfigurator.configure())
    install(RequestValidation, ValidatorConfigurator.configure())

    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
    transaction {
        SchemaUtils.create(CountersEntity)
    }

    configureRoutes()
}