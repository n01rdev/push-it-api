package com.nebrija.pushit.api.security.application.service

import com.nebrija.pushit.api.security.application.mapper.SecurityMapper
import com.nebrija.pushit.api.security.domain.model.Security
import com.nebrija.pushit.api.security.domain.service.ICreateSecurityService
import com.nebrija.pushit.api.security.infrastructure.db.postgres.repository.SecurityRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CreateSecurityService(
    private val securityRepository: SecurityRepository,
    private val passwordEncoder: PasswordEncoder,
    private val securityMapper: SecurityMapper,
    private val jweService: JweService
) : ICreateSecurityService {

    override fun create(security: Security): String {
        val user = User.builder()
            .username(security.email)
            .password(passwordEncoder.encode(security.password))
            .roles(*security.roles.toTypedArray())
            .build()

        val securityModel = Security(
            email = user.username,
            password = user.password,
            roles = user.authorities.map { it.authority }
        )

        val claims = mapOf(
            "username" to user.username,
            "password" to user.password,
            "roles" to user.authorities.map { it.authority }
        )

        val token = jweService.generateToken(claims, user.username)

        securityRepository.save(securityMapper.toEntity(securityModel))

        return token
    }
}