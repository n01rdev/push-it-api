package com.nebrija.pushit.api.posit.domain.repository

import com.nebrija.pushit.api.posit.domain.model.Posit

interface IPositRepository {
    fun save(posit: Posit): Posit
    fun delete(posit: Posit)
    fun findByTitle(title: String): Posit?
    fun findByUuid(uuid: String): Posit?
    fun findAll(): List<Posit>
}