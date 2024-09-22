package com.example.routes.counters

import com.example.dto.request.CounterCreateRequest
import com.example.dto.response.CountersResponse
import com.example.service.CountersService
import io.github.tabilzad.ktor.annotations.KtorResponds
import io.github.tabilzad.ktor.annotations.ResponseEntry
import io.github.tabilzad.ktor.annotations.Tag
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

@Tag(["Counters"])
fun Routing.countersRoutes() {
    route("/counters") {
        @KtorResponds(
            mapping = [
                ResponseEntry("200", CountersResponse::class),
                ResponseEntry("404", Unit::class)
            ]
        )
        get("/read") {
            val counter = call.request.queryParameters["counter"]
            if (counter == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get
            }

            val result = transaction { CountersService.get(counter) }

            if (result == null) call.respond(HttpStatusCode.NotFound)
            else call.respond(HttpStatusCode.OK, result.toResponse())
        }

        @KtorResponds(
            mapping = [
                ResponseEntry("201", CountersResponse::class),
            ]
        )
        post("/create") {
            val request = call.receive<CounterCreateRequest>()
            val result = transaction { CountersService.create(request.name, request.counter) }

            call.respond(HttpStatusCode.Created, result.toResponse())
        }

        @KtorResponds(
            mapping = [
                ResponseEntry("422", Unit::class),
                ResponseEntry("200", Int::class)
            ]
        )
        patch("/increment") {
            val counter = call.request.queryParameters["counter"]
            if (counter == null) {
                call.respond(HttpStatusCode.UnprocessableEntity)
                return@patch
            }

            val result = transaction { CountersService.increment(counter) }

            if (result == null) call.respond(HttpStatusCode.UnprocessableEntity)
            else call.respond(HttpStatusCode.OK, result)
        }

        @KtorResponds(
            mapping = [
                ResponseEntry("204", Unit::class)
            ]
        )
        delete("/delete") {
            val counter = call.request.queryParameters["counter"]
            if (counter == null) {
                call.respond(HttpStatusCode.NoContent)
                return@delete
            }

            transaction { CountersService.delete(counter) }
            call.respond(HttpStatusCode.NoContent)
        }

        @KtorResponds(
            mapping = [
                ResponseEntry("200", CountersResponse::class, isCollection = true),
            ]
        )
        get("/all") {
            val result = transaction { CountersService.getAll().map { it.toResponse() } }

            call.respond(HttpStatusCode.OK, result)
        }
    }
}