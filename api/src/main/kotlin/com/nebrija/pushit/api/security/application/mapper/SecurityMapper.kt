package com.nebrija.pushit.api.security.application.mapper

import com.nebrija.pushit.api.roles.infrastructure.db.postgres.repository.IRoleRepository
import com.nebrija.pushit.api.security.domain.model.Security
import com.nebrija.pushit.api.security.infrastructure.db.postgres.entity.SecurityEntity
import org.springframework.stereotype.Component

@Component
class SecurityMapper(
    private val roleRepository: IRoleRepository
) {

    fun toEntity(security: Security): SecurityEntity {
        val userRole = roleRepository.findByName("User")?: throw Exception("User role not found")
        return SecurityEntity(
            email = security.email,
            password = security.password,
            active = true,
            roles = listOf(userRole).toSet()
        )
    }

    fun toModel(securityEntity: SecurityEntity): Security {
        return Security(
            email = securityEntity.email,
            password = securityEntity.password,
            roles = securityEntity.authorities.map { it.authority }
        )
    }
}
