package com.example.configuration

import io.ktor.server.plugins.requestvalidation.*
import com.example.dto.request.CounterCreateRequest

object ValidatorConfigurator {
    fun configure(): RequestValidationConfig.() -> Unit = {
        validate(CounterCreateRequest)
    }
}