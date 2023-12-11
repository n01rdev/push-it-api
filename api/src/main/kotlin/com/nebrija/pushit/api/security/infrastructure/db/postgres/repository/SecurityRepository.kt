package com.nebrija.pushit.api.security.infrastructure.db.postgres.repository

import com.nebrija.pushit.api.security.domain.repository.ISecurityRepository
import com.nebrija.pushit.api.security.infrastructure.db.postgres.entity.SecurityEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class SecurityRepository(
    @PersistenceContext
    private val entityManager: EntityManager,
    private val jpaRepository: ISecurityJpaRepository
) : ISecurityRepository {

    override fun save(securityEntity: SecurityEntity) {
        entityManager.persist(securityEntity)
        entityManager.flush()
    }

    override fun delete(securityEntity: SecurityEntity) {
        securityEntity.active = false
        entityManager.merge(securityEntity)
        entityManager.flush()
    }

    override fun findByUuid(uuid: UUID): SecurityEntity? {
        return jpaRepository.findByUuidAndActiveTrue(uuid)
    }

    override fun findByEmail(email: String): SecurityEntity? {
        return jpaRepository.findByEmailAndActiveTrue(email)
    }
}
