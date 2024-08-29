package com.example.dto.request

import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

@Serializable
data class CounterCreateRequest(
    val name: String,
    val counter: Int
) {
    companion object Validate : Validator {
        override fun filter(value: Any): Boolean = value is CounterCreateRequest

        override suspend fun validate(value: Any): ValidationResult = buildList {
            check(value is CounterCreateRequest)
            if(value.name.isBlank()) add("name can't be blank")
            if (value.counter < 0) add("value can't be negative")
        }.takeIf { it.isNotEmpty() }?.let { ValidationResult.Invalid(it) } ?: ValidationResult.Valid
    }
}