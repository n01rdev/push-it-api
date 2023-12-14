package com.nebrija.pushit.api.security.config

import com.nebrija.pushit.api.security.application.service.JweService
import com.nebrija.pushit.api.security.domain.exception.JwtEmailExtractionException
import com.nebrija.pushit.api.security.domain.exception.JwtTokenInvalidException
import com.nebrija.pushit.api.security.domain.exception.JwtTokenMissingException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JweAuthenticationFilter(
    private val jweService: JweService,
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    val jweLogger: Logger = LoggerFactory.getLogger(JweService::class.java)

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.requestURI.startsWith("/api/v1/security/")
    }

    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val uri = request.requestURI
            if (!uri.startsWith("/api/v1/security/")) {
                val token = extractToken(request)

                if (token != null) {
                    val email = jweService.extractEmail(token) ?: run {
                        jweLogger.error("Error extracting email from token")
                        throw JwtEmailExtractionException()
                    }
                    if (!jweService.isTokenValid(token, email)) {
                        jweLogger.error("Error validating token")
                        throw JwtTokenInvalidException()
                    }

                    jweLogger.info("Email extracted from token: $email")

                    val userDetails = userDetailsService.loadUserByUsername(email)
                    val authentication = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                } else {
                    jweLogger.info("No token found")
                }
            }

            filterChain.doFilter(request, response)

        } catch (ex: JwtEmailExtractionException) {
            jweLogger.error("Error extracting email from token: ${ex.message}")
        } catch (ex: JwtTokenInvalidException) {
            jweLogger.error("Error validating token: ${ex.message}")
        } catch (ex: JwtTokenMissingException) {
            jweLogger.error("Error validating token: ${ex.message}")
        }
    }

    fun extractToken(request: HttpServletRequest): String? {

        try {
            val header = request.getHeader("Authorization")
            if (header != null && header.startsWith("Bearer ")) {
                return header.substring(7)
            }
            return null
        } catch (ex: JwtTokenMissingException) {
            jweLogger.error("Error extracting token: ${ex.message}")
            throw JwtTokenMissingException()
        }
    }
}