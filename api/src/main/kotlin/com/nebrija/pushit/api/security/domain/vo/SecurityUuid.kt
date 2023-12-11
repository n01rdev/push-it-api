package com.nebrija.pushit.api.security.domain.vo

import java.util.UUID

data class SecurityUuid(
    val value: UUID = UUID.randomUUID()
)
