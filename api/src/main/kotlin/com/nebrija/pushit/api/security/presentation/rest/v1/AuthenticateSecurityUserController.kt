package com.nebrija.pushit.api.security.presentation.rest.v1

import com.nebrija.pushit.api.security.application.service.AuthenticateSecurityService
import com.nebrija.pushit.api.security.domain.exception.InvalidCredentialsException
import com.nebrija.pushit.api.security.domain.model.Security
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/security")
class AuthenticateSecurityUserController(
    private val authenticateSecurityService: AuthenticateSecurityService
) {
    @PostMapping("/login")
    fun authenticate(@RequestBody security: Security): ResponseEntity<String> {
        return try {
            val token = authenticateSecurityService.authenticate(security.email, security.password)
            ResponseEntity.ok(token)
        } catch (e: InvalidCredentialsException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}