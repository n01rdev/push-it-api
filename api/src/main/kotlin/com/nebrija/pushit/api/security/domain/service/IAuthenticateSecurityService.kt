package com.nebrija.pushit.api.security.domain.service

fun interface IAuthenticateSecurityService {

    fun authenticate(email: String, password: String): String
}
