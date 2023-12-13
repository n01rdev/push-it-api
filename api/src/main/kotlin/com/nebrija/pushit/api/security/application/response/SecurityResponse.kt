package com.nebrija.pushit.api.security.application.response

import org.springframework.web.bind.annotation.ResponseBody

@ResponseBody
data class SecurityResponse(
    val token: String,
    val uuid: String
)
