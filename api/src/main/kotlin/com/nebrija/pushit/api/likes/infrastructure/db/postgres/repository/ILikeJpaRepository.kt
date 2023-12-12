package com.nebrija.pushit.api.likes.infrastructure.db.postgres.repository

import com.nebrija.pushit.api.likes.infrastructure.db.postgres.entity.LikeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ILikeJpaRepository: JpaRepository<LikeEntity, Int> {
    fun findByUserUuidAndPositUuid(userUuid: String, positUuid: String): LikeEntity?
}