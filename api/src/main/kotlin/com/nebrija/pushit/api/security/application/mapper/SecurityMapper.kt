package com.nebrija.pushit.api.security.application.mapper

import com.nebrija.pushit.api.security.domain.model.Security
import com.nebrija.pushit.api.security.infrastructure.db.postgres.entity.SecurityEntity
import org.springframework.stereotype.Component

@Component
class SecurityMapper {

    fun toEntity(security: Security): SecurityEntity {
        return SecurityEntity(
            email = security.email,
            password = security.password
        )
    }

    fun toModel(securityEntity: SecurityEntity): Security {
        return Security(
            email = securityEntity.email,
            password = securityEntity.password
        )
    }
}