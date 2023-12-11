package com.nebrija.pushit.api.security.application.request

data class SecurityUserRequest(
    val email: String,
    val password: String,
    val roles: List<String>
)
