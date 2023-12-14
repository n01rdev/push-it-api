package com.nebrija.pushit.api.security.domain.exception

class JwtTokenMissingException : RuntimeException("JWT token is missing from request")