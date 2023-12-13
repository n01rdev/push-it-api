package com.nebrija.pushit.api.security.infrastructure.db.postgres.repository

import com.nebrija.pushit.api.security.application.mapper.SecurityMapper
import com.nebrija.pushit.api.security.domain.model.Security
import com.nebrija.pushit.api.security.domain.repository.ISecurityRepository
import com.nebrija.pushit.api.security.infrastructure.db.postgres.entity.SecurityEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository

@Repository
class SecurityRepository(
    @PersistenceContext
    private val entityManager: EntityManager,
    private val jpaRepository: ISecurityJpaRepository,
    private val securityMapper: SecurityMapper
) : ISecurityRepository {

    override fun save(security: Security): String {
        val securityEntity = securityMapper.toEntity(security)
        entityManager.persist(securityEntity)
        entityManager.flush()
        return securityEntity.uuid
    }

    override fun delete(security: Security) {
        val securityEntity = securityMapper.toEntity(security)
        securityEntity.active = false
        securityEntity.deletedAt = java.time.LocalDateTime.now()
        entityManager.merge(securityEntity)
        entityManager.flush()
    }

    override fun findByUuid(uuid: String): Security? {
        val securityEntity = jpaRepository.findByUuidAndActiveTrue(uuid)
        return securityEntity?.let { securityMapper.toModel(it) }
    }

    fun findByUuidEntity(uuid: String): SecurityEntity? {
        return jpaRepository.findByUuidAndActiveTrue(uuid)
    }

    override fun findByEmail(email: String): Security? {
        val securityEntity = jpaRepository.findByEmailAndActiveTrue(email)
        return securityEntity?.let { securityMapper.toModel(it) }
    }

    fun findByEmailEntity(email: String): SecurityEntity? {
        return jpaRepository.findByEmailAndActiveTrue(email)
    }
}
