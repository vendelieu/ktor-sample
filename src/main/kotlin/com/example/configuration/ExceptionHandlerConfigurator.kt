package com.example.configuration

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.logging.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException
import org.slf4j.LoggerFactory

private fun Throwable.getMessage() = message ?: "Unknown error occurred!"

object ExceptionHandlerConfigurator {
    private val logger = LoggerFactory.getLogger(javaClass)

    fun configure(): StatusPagesConfig.() -> Unit = {
        exception<SerializationException> { call, ex ->
            call.logEx(ex)
            call.respond(HttpStatusCode.BadRequest, ex.getMessage())
        }

        exception<IllegalArgumentException> { call, ex ->
            call.logEx(ex)
            call.respond(HttpStatusCode.BadRequest, ex.getMessage())
        }

        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString())
        }

        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                "Unknown error occurred! Please contact your server administrator!"
            )
            throw cause
        }
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun <T : Throwable> ApplicationCall.logEx(ex: T) {
        logger.error("Occurred error while processing: ${request.toLogString()}", ex)
    }
}