package com.nebrija.pushit.api.security.domain.exception

class JwtEmailExtractionException: RuntimeException("Failed to extract email from JWT token")