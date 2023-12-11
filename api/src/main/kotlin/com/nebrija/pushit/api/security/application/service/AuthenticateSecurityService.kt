package com.nebrija.pushit.api.security.application.service

import com.nebrija.pushit.api.security.domain.exception.InvalidCredentialsException
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
    override fun authenticate(email: String, password: String): String {
        val user = securityRepository.findByEmailEntity(email)
        if (user == null || !passwordEncoder.matches(password, user.password)) {
            throw InvalidCredentialsException()
        }
        val claims = mapOf(
            "email" to user.email,
            "authorities" to user.authorities.map { it.authority }
        )
        return jweService.generateToken(claims, user.email)
    }
}
