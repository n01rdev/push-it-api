package com.nebrija.pushit.api.security.application.service

import com.nebrija.pushit.api.security.application.mapper.SecurityMapper
import com.nebrija.pushit.api.security.application.response.SecurityResponse
import com.nebrija.pushit.api.security.domain.exception.InvalidCredentialsException
import com.nebrija.pushit.api.security.domain.model.Security
import com.nebrija.pushit.api.security.domain.service.IAuthenticateSecurityService
import com.nebrija.pushit.api.security.infrastructure.db.postgres.repository.SecurityRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticateSecurityService(
    private val securityRepository: SecurityRepository,
    private val jweService: JweService,
    private val passwordEncoder: PasswordEncoder,
    private val securityMapper: SecurityMapper
): IAuthenticateSecurityService{
    override fun authenticate(security: Security): SecurityResponse {
        val jweLogger = LoggerFactory.getLogger(JweService::class.java)

        jweLogger.info("Authenticating user with email: ${security.email}")

        val email = security.email
        val password = security.password

        val userEntity = securityRepository.findByEmailEntity(email)

        if (userEntity == null || !passwordEncoder.matches(password, userEntity.password)) {
            throw InvalidCredentialsException()
        }

        val token = jweService.generateToken(securityMapper.toModel(userEntity))

        return SecurityResponse(
            token = token,
            uuid = userEntity.uuid
        )
    }
}
