package com.nebrija.pushit.api.security.domain.service

interface IJweService {
    fun generateToken(claims: Map<String, Any>, username: String): String
    fun isTokenValid(token: String, username: String): Boolean
    fun isTokenExpired(token: String): Boolean
    fun extractEmail(token: String): String?
    fun extractAllClaims(token: String): Map<String, Any>?
    fun encode(claims: Map<String, Any>): String
    fun decode(token: String): Map<String, Any>?
}