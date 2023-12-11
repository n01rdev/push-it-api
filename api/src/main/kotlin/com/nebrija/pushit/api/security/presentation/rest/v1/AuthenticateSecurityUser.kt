package com.nebrija.pushit.api.security.presentation.rest.v1

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/security")
class AuthenticateSecurityUser {

    @PostMapping("/login")
    fun authenticate() {
        //TODO
    }
}