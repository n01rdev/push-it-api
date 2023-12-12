package com.nebrija.pushit.api.posit.infrastructure.db.postgres.repository

import com.nebrija.pushit.api.posit.infrastructure.db.postgres.entity.PositEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IPositJpaRepository: JpaRepository<PositEntity, Int> {

    fun findByTitleAndActiveTrue(title: String): PositEntity?
    fun findByUuidAndActiveTrue(uuid: String): PositEntity?
    fun findAllByActiveTrue(): List<PositEntity>
}
