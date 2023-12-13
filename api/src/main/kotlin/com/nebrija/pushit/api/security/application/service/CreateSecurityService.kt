package com.nebrija.pushit.api.security.application.service

import com.nebrija.pushit.api.roles.infrastructure.db.postgres.repository.IRoleRepository
import com.nebrija.pushit.api.security.application.response.SecurityResponse
import com.nebrija.pushit.api.security.domain.exception.UserAlreadyExistsException
import com.nebrija.pushit.api.security.domain.model.Security
import com.nebrija.pushit.api.security.domain.service.ICreateSecurityService
import com.nebrija.pushit.api.security.infrastructure.db.postgres.repository.SecurityRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateSecurityService(
    private val securityRepository: SecurityRepository,
    private val roleRepository: IRoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jweService: JweService,
) : ICreateSecurityService {

    @Transactional
    override fun create(security: Security): SecurityResponse{
        val userRole = roleRepository.findByName("User")
        val existingUser = securityRepository.findByEmailEntity(security.email)

        if (userRole == null) {
            throw RuntimeException("User role not found")
        }

        if (existingUser != null) {
            throw UserAlreadyExistsException()
        }

        val user = User.builder()
            .username(security.email)
            .password(passwordEncoder.encode(security.password))
            .roles(userRole.name)
            .build()

        val securityModel = Security(
            email = user.username,
            password = user.password,
            roles = user.authorities.map { it.authority }
        )

        val claims = mapOf(
            "email" to user.username,
            "authorities" to user.authorities.map { it.authority }
        )

        val token = jweService.generateToken(claims, user.username)

        val uuid = securityRepository.save(securityModel)

        return SecurityResponse(
            token = token,
            uuid = uuid
        )
    }
}