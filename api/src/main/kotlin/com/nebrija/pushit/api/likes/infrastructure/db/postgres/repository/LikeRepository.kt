package com.nebrija.pushit.api.likes.infrastructure.db.postgres.repository

import com.nebrija.pushit.api.likes.application.mapper.LikeMapper
import com.nebrija.pushit.api.likes.domain.model.Like
import com.nebrija.pushit.api.likes.domain.repository.ILikeRepository
import com.nebrija.pushit.api.likes.infrastructure.db.postgres.entity.LikeEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository

@Repository
class LikeRepository(
    @PersistenceContext
    private val entityManager: EntityManager,
    private val jpaRepository: ILikeJpaRepository,
    private val likeMapper: LikeMapper
): ILikeRepository {
    override fun save(like: Like): Like {
        val likeEntity = likeMapper.toEntity(like)
        entityManager.persist(likeEntity)
        entityManager.flush()
        return like
    }

    override fun findByUserAndPosit(user: String, posit: String): Like? {
        val likeEntity = jpaRepository.findByUserUuidAndPositUuid(user, posit)
        return likeEntity?.let { likeMapper.toModel(it) }
    }

    fun findByUserAndPositEntity(user: String, posit: String): LikeEntity? {
        return jpaRepository.findByUserUuidAndPositUuid(user, posit)
    }

}