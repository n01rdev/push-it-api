package com.nebrija.pushit.api.security.domain.model

data class Security(
    val email: String,
    val password: String,
    val roles: List<String> = listOf()
)