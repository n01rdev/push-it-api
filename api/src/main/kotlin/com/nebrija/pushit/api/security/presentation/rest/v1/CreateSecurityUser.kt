package com.nebrija.pushit.api.security.presentation.rest.v1

import com.nebrija.pushit.api.security.application.service.CreateSecurityService
import com.nebrija.pushit.api.security.domain.model.Security
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
@RestController
@RequestMapping("/api/v1/security")
class CreateSecurityUser(private val createSecurityService: CreateSecurityService) {

    @PostMapping("/register")
    fun register(@RequestBody security: Security): ResponseEntity<String> {
        val token = createSecurityService.create(security)
        return ResponseEntity.status(HttpStatus.CREATED).body(token)
    }
}