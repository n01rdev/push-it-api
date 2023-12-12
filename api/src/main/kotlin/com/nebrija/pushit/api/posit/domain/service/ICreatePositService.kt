package com.nebrija.pushit.api.posit.domain.service

import com.nebrija.pushit.api.posit.domain.model.Posit

fun interface ICreatePositService {
    fun create(posit: Posit): String
}