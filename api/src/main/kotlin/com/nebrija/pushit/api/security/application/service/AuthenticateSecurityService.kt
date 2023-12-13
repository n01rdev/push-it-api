package com.nebrija.pushit.api.security.application.service

import com.nebrija.pushit.api.security.application.response.SecurityResponse
import com.nebrija.pushit.api.security.domain.exception.InvalidCredentialsException
import com.nebrija.pushit.api.security.domain.model.Security
import com.nebrija.pushit.api.security.domain.service.IAuthenticateSecurityService
import com.nebrija.pushit.api.security.infrastructure.db.postgres.repository.SecurityRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticateSecurityService(
    private val securityRepository: SecurityRepository,
    private val jweService: JweService,
    private val passwordEncoder: PasswordEncoder,
): IAuthenticateSecurityService{
    override fun authenticate(security: Security): SecurityResponse {
        val email = security.email
        val password = security.password
        val user = securityRepository.findByEmailEntity(email)
        if (user == null || !passwordEncoder.matches(password, user.password)) {
            throw InvalidCredentialsException()
        }
        val claims = mapOf(
            "email" to user.email,
            "authorities" to user.authorities.map { it.authority }
        )

        val token = jweService.generateToken(claims, user.email)

        return SecurityResponse(
            token = token,
            uuid = user.uuid
        )
    }
}
