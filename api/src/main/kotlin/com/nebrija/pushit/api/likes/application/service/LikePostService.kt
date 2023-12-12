package com.nebrija.pushit.api.likes.application.service

import com.nebrija.pushit.api.likes.application.mapper.LikeMapper
import com.nebrija.pushit.api.likes.domain.exception.UserHasAlreadyLikedPostException
import com.nebrija.pushit.api.likes.domain.model.Like
import com.nebrija.pushit.api.likes.domain.service.ILikePostService
import com.nebrija.pushit.api.likes.infrastructure.db.postgres.repository.LikeRepository
import com.nebrija.pushit.api.posit.application.mapper.PositMapper
import com.nebrija.pushit.api.posit.domain.exception.AuthorNotFoundException
import com.nebrija.pushit.api.posit.domain.exception.PositNotFoundException
import com.nebrija.pushit.api.posit.infrastructure.db.postgres.repository.PositRepository
import com.nebrija.pushit.api.security.infrastructure.db.postgres.repository.SecurityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LikePostService(
    private val likeRepository: LikeRepository,
    private val positRepository: PositRepository,
    private val securityRepository: SecurityRepository,
    private val likeMapper: LikeMapper,
    private val positMapper: PositMapper
): ILikePostService {
    @Transactional
    override fun likePost(postUuid: String, userUuid: String): String {
        val author = securityRepository.findByUuidEntity(userUuid)
        val posit = positRepository.findByUuidEntity(postUuid)

        if (author == null) {
            throw AuthorNotFoundException()
        }

        if (posit == null) {
            throw PositNotFoundException()
        }

        if(likeRepository.findByUserAndPositEntity(userUuid, postUuid) != null) {
            throw UserHasAlreadyLikedPostException()
        }

        val like = Like(userUuid = userUuid, positUuid = postUuid)
        val savedLike = likeRepository.save(like)
        val savedLikeEntity = likeMapper.toEntity(savedLike)

        posit.addLike(savedLikeEntity)
        positRepository.save(positMapper.toModel(posit))

        return savedLikeEntity.uuid
    }
}