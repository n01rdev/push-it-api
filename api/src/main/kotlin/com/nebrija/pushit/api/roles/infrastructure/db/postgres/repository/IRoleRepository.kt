package com.nebrija.pushit.api.roles.infrastructure.db.postgres.repository

import com.nebrija.pushit.api.roles.infrastructure.db.postgres.entity.RoleEntity
import org.springframework.data.jpa.repository.JpaRepository

interface IRoleRepository : JpaRepository<RoleEntity, Long> {
    fun findByName(name: String): RoleEntity?
}