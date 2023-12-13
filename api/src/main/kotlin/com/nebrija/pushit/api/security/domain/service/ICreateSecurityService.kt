package com.nebrija.pushit.api.security.domain.service

import com.nebrija.pushit.api.security.application.response.SecurityResponse
import com.nebrija.pushit.api.security.domain.model.Security

fun interface ICreateSecurityService {
    fun create(security: Security): SecurityResponse
}