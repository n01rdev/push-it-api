package com.nebrija.pushit.api.posit.application.mapper

import com.nebrija.pushit.api.posit.domain.model.Posit
import com.nebrija.pushit.api.posit.infrastructure.db.postgres.entity.PositEntity
import com.nebrija.pushit.api.security.infrastructure.db.postgres.repository.SecurityRepository
import org.springframework.stereotype.Component

@Component
class PositMapper(
    private val securityRepository: SecurityRepository
) {
    fun toModel(entity: PositEntity): Posit {
        return Posit(
            title = entity.title,
            content = entity.content,
            authorUuid = entity.author.uuid
        )
    }


    fun toEntity(model: Posit): PositEntity {
        val author = securityRepository.findByUuidEntity(model.authorUuid)
            ?: throw Exception("Author not found")
        return PositEntity(
            title = model.title,
            content = model.content,
            author = author
        )
    }
}