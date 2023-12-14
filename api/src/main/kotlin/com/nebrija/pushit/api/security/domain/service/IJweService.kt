package com.nebrija.pushit.api.security.domain.service

import com.nebrija.pushit.api.security.domain.model.Security

interface IJweService {
    fun generateToken(security: Security): String
    fun generateClaims(security: Security): Map<String, Any>
    fun validateToken(token: String, username: String)
    fun isTokenValid(token: String, username: String): Boolean
    fun isTokenExpired(token: String): Boolean
    fun extractEmail(token: String): String?
    fun extractAllClaims(token: String): Map<String, Any>?
    fun encode(claims: Map<String, Any>): String
    fun decode(token: String): Map<String, Any>?
}