package com.nebrija.pushit.api.security.presentation.rest.v1

import com.nebrija.pushit.api.security.application.request.SecurityUserRequest
import com.nebrija.pushit.api.security.application.service.CreateSecurityService
import com.nebrija.pushit.api.security.domain.model.Security
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/security")
class CreateSecurityUser(private val createSecurityService: CreateSecurityService) {

    @PostMapping("/register")
    fun register(@RequestBody request: SecurityUserRequest): ResponseEntity<String> {

        val security = Security(
            email = request.email,
            password = request.password,
            roles = request.roles
        )

        val token = createSecurityService.create(security)

        return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(token)
                .toUri()
        ).body(token)
    }
}