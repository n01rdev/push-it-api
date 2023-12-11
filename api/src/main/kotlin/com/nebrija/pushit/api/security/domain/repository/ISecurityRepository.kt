package com.nebrija.pushit.api.security.domain.repository

import com.nebrija.pushit.api.security.domain.model.Security


interface ISecurityRepository {
    fun save(security: Security)
    fun delete(security: Security)
    fun findByUuid(uuid: String): Security?
    fun findByEmail(email: String): Security?
}
