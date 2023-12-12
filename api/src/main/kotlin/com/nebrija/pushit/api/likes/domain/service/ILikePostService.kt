package com.nebrija.pushit.api.likes.domain.service

fun interface ILikePostService {
    fun likePost(postUuid: String, userUuid: String): String
}