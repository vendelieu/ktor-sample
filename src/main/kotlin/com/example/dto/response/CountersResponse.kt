package com.example.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class CountersResponse(
    val name: String,
    val counter: Int
)