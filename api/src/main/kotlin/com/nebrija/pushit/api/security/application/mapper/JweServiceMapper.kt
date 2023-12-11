package com.nebrija.pushit.api.security.application.mapper

import com.nimbusds.jwt.JWTClaimsSet
import org.springframework.stereotype.Component

@Component
class JweServiceMapper {

    fun toClaimsSet(claims: Map<String, Any>): JWTClaimsSet {
        return JWTClaimsSet.Builder().claim("claims", claims).build()
    }

    fun fromClaimsSet(jwtClaimsSet: JWTClaimsSet): Map<String, Any>? {
        return jwtClaimsSet.claims
    }
}