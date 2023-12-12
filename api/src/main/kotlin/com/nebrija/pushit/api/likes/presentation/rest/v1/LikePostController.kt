package com.nebrija.pushit.api.likes.presentation.rest.v1

import com.nebrija.pushit.api.likes.application.service.LikePostService
import com.nebrija.pushit.api.likes.domain.exception.UserHasAlreadyLikedPostException
import com.nebrija.pushit.api.likes.domain.model.Like
import com.nebrija.pushit.api.posit.domain.exception.AuthorNotFoundException
import com.nebrija.pushit.api.posit.domain.exception.PositNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/")
class LikePostController(
    private val likePostService: LikePostService
) {

    @PostMapping("/like")
    fun likePost(@RequestBody like: Like): ResponseEntity<String> {
        return try {
            val uuid = likePostService.likePost(like.positUuid, like.userUuid)
            ResponseEntity.status(HttpStatus.CREATED).body(uuid)
        } catch (e: AuthorNotFoundException)
        {
            ResponseEntity.badRequest().body(e.message)
        } catch (e: PositNotFoundException)
        {
            ResponseEntity.badRequest().body(e.message)
        } catch (e: UserHasAlreadyLikedPostException)
        {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        } catch (e: Exception)
        {
            ResponseEntity.internalServerError().body(e.message)
        }
    }
}
