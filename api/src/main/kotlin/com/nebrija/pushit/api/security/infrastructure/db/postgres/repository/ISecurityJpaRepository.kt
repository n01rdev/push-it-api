package com.nebrija.pushit.api.security.infrastructure.db.postgres.repository

import com.nebrija.pushit.api.security.infrastructure.db.postgres.entity.SecurityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
@Repository
interface ISecurityJpaRepository : JpaRepository<SecurityEntity, Int> {

    fun findByEmailAndActiveTrue(email: String): SecurityEntity?
    fun findByUuidAndActiveTrue(uuid: String): SecurityEntity?
}