package com.nebrija.pushit.api.likes.domain.repository

import com.nebrija.pushit.api.likes.domain.model.Like

interface ILikeRepository {
    fun save(like: Like): Like
    fun findByUserAndPosit(user: String, posit: String): Like?
}