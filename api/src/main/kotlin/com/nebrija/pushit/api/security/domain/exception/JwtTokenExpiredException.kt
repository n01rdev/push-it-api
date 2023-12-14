package com.nebrija.pushit.api.security.domain.exception

class JwtTokenExpiredException : RuntimeException("JWT token is expired")