package com.nebrija.pushit.api.security.domain.repository

import com.nebrija.pushit.api.security.infrastructure.db.postgres.entity.SecurityEntity
import java.util.UUID


interface ISecurityRepository {
    fun save(securityEntity: SecurityEntity)
    fun delete(securityEntity: SecurityEntity)
    fun findByUuid(uuid: UUID): SecurityEntity?
    fun findByEmail(email: String): SecurityEntity?
}
