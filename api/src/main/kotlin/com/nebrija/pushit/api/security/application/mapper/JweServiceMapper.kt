package com.nebrija.pushit.api.security.application.mapper

import com.nimbusds.jwt.JWTClaimsSet
import org.springframework.stereotype.Component

@Component
class JweServiceMapper {

    fun toClaimsSet(claims: Map<String, Any>): JWTClaimsSet {
        return JWTClaimsSet.Builder().claim("claims", claims).build()
    }

    fun fromClaimsSet(jwtClaimsSet: JWTClaimsSet): Map<String, Any>? {
        val claim = jwtClaimsSet.getClaim("claims")
        return if (claim is Map<*, *>) {
            @Suppress("UNCHECKED_CAST")
            claim as Map<String, Any>
        } else {
            null
        }
    }
}