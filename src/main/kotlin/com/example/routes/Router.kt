package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.example.routes.counters.countersRoutes
import com.example.routes.openapi.openApiRoutes
import io.github.tabilzad.ktor.annotations.GenerateOpenApi
import org.slf4j.LoggerFactory

@GenerateOpenApi
fun Application.configureRoutes() {
    val logger = LoggerFactory.getLogger(javaClass)
    routing {
        trace { logger.trace(it.buildText()) }

        openApiRoutes()
        countersRoutes()
    }
}