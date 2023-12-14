package com.nebrija.pushit.api.security.config

import com.nebrija.pushit.api.security.application.service.JweService
import com.nebrija.pushit.api.security.domain.exception.JwtEmailExtractionException
import com.nebrija.pushit.api.security.domain.exception.JwtTokenInvalidException
import com.nebrija.pushit.api.security.domain.exception.JwtTokenMissingException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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

    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = extractToken(request)

            if (token != null) {
                val username = jweService.extractEmail(token)
                if (username != null) {
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    jweService.validateToken(token, username)
                    val authentication = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }

            filterChain.doFilter(request, response)
        } catch (ex: JwtEmailExtractionException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.message)
        } catch (ex: JwtTokenInvalidException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.message)
        } catch (ex: JwtTokenMissingException) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.message)
        }
    }

    fun extractToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }
}