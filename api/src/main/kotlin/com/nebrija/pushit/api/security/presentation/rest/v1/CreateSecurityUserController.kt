package com.nebrija.pushit.api.security.presentation.rest.v1

import com.nebrija.pushit.api.security.application.service.CreateSecurityService
import com.nebrija.pushit.api.security.domain.exception.UserAlreadyExistsException
import com.nebrija.pushit.api.security.domain.model.Security
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/security")
class CreateSecurityUserController(
    private val createSecurityService: CreateSecurityService
) {
    @PostMapping("/register")
    fun create(@RequestBody security: Security): ResponseEntity<Any> {
        return try {
            val response = createSecurityService.create(security)
            ResponseEntity.ok(response)
        } catch (e: UserAlreadyExistsException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.internalServerError().body(e.message)
        }
    }
}