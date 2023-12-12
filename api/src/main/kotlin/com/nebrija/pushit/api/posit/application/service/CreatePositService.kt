package com.nebrija.pushit.api.posit.application.service

import com.nebrija.pushit.api.posit.application.mapper.PositMapper
import com.nebrija.pushit.api.posit.domain.exception.AuthorNotFoundException
import com.nebrija.pushit.api.posit.domain.exception.PositAlreadyExistsException
import com.nebrija.pushit.api.posit.domain.model.Posit
import com.nebrija.pushit.api.posit.domain.service.ICreatePositService
import com.nebrija.pushit.api.posit.infrastructure.db.postgres.repository.PositRepository
import com.nebrija.pushit.api.security.infrastructure.db.postgres.repository.SecurityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreatePositService(
    private val positRepository: PositRepository,
    private val securityRepository: SecurityRepository,
    private val positMapper: PositMapper
) : ICreatePositService {

    @Transactional
    override fun create(posit: Posit): String {
        val existingAuthor = securityRepository.findByUuidEntity(posit.authorUuid)
        val existingPosit = positRepository.findByTitle(posit.title)

        if (existingAuthor == null) {
            throw AuthorNotFoundException()
        }

        if (existingPosit != null) {
            throw PositAlreadyExistsException()
        }

        val savedPosit = positRepository.save(posit)
        val positEntity = positMapper.toEntity(savedPosit)
        return positEntity.uuid
    }
}
