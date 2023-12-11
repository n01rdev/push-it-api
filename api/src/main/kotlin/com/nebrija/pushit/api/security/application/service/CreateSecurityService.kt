package com.nebrija.pushit.api.security.application.service

import com.nebrija.pushit.api.roles.infrastructure.db.postgres.repository.IRoleRepository
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
    private val jweService: JweService
) : ICreateSecurityService {

    @Transactional
    override fun create(security: Security): String {
        val userRole = roleRepository.findByName("User")
        val user = User.builder()
            .username(security.email)
            .password(passwordEncoder.encode(security.password))
            .roles(userRole!!.name)
            .build()

        val securityModel = Security(
            email = user.username,
            password = user.password,
            roles = user.authorities.map { it.authority }
        )

        val claims = mapOf(
            "email" to user.username,
            "password" to user.password,
            "authorities" to user.authorities.map { it.authority }
        )

        val token = jweService.generateToken(claims, user.username)

        securityRepository.save(securityModel)

        return token
    }
}