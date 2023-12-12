package com.nebrija.pushit.api.posit.infrastructure.db.postgres.repository

import com.nebrija.pushit.api.posit.application.mapper.PositMapper
import com.nebrija.pushit.api.posit.domain.model.Posit
import com.nebrija.pushit.api.posit.domain.repository.IPositRepository
import com.nebrija.pushit.api.posit.infrastructure.db.postgres.entity.PositEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository

@Repository
class PositRepository(
    @PersistenceContext
    private val entityManager: EntityManager,
    private val jpaRepository: IPositJpaRepository,
    private val positMapper: PositMapper
): IPositRepository {

    override fun save(posit: Posit): Posit {
        val positEntity = positMapper.toEntity(posit)
        entityManager.persist(positEntity)
        entityManager.flush()
        return posit
    }

    override fun delete(posit: Posit) {
        val positEntity = positMapper.toEntity(posit)
        positEntity.active = false
        positEntity.deletedAt = java.time.LocalDateTime.now()
        entityManager.merge(positEntity)
        entityManager.flush()
    }

    override fun findByTitle(title: String): Posit? {
        val positEntity = jpaRepository.findByTitleAndActiveTrue(title)
        return positEntity?.let { positMapper.toModel(it) }
    }

    override fun findByUuid(uuid: String): Posit? {
        val positEntity = jpaRepository.findByUuidAndActiveTrue(uuid)
        return positEntity?.let { positMapper.toModel(it) }
    }

    fun findByUuidEntity(uuid: String): PositEntity? {
        return jpaRepository.findByUuidAndActiveTrue(uuid)
    }

    override fun findAll(): List<Posit> {
        val positEntities = jpaRepository.findAllByActiveTrue()
        return positEntities.map { positMapper.toModel(it) }
    }
}
