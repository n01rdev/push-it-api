package com.nebrija.pushit.api.security.application.mapper

import com.nebrija.pushit.api.security.domain.model.Security
import com.nimbusds.jwt.JWTClaimsSet
import org.springframework.stereotype.Component
import java.util.*

@Component
class JweServiceMapper {

    fun toClaimsSet(claims: Map<String, Any>): JWTClaimsSet {
        return JWTClaimsSet.Builder().claim("claims", claims).build()
    }

    fun fromClaimsSet(jwtClaimsSet: JWTClaimsSet): Map<String, Any>? {
        return jwtClaimsSet.claims
    }

    fun toClaims(security: Security): Map<String, Any> {
        val claims = mutableMapOf<String, Any>()
        claims["email"] = security.email
        claims["roles"] = security.roles
        claims["exp"] = Date(Date().time + 3600 * 1000) // 1 Hour
        return claims
    }
}