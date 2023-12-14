package com.nebrija.pushit.api.core.debug.testJweFilter

import com.nebrija.pushit.api.security.application.service.JweService
import com.nebrija.pushit.api.security.config.JweAuthenticationFilter
import com.nebrija.pushit.api.security.domain.exception.JwtEmailExtractionException
import com.nebrija.pushit.api.security.domain.exception.JwtTokenInvalidException
import com.nebrija.pushit.api.security.domain.exception.JwtTokenMissingException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource

class Test {

    @Test
    fun testJweAuthenticationFilter() {
        val request = Mockito.mock(HttpServletRequest::class.java)
        val response = Mockito.mock(HttpServletResponse::class.java)
        val filterChain = Mockito.mock(FilterChain::class.java)

        // Crear instancias de JweService y UserDetailsService
        val jweService = Mockito.mock(JweService::class.java)
        val userDetailsService = Mockito.mock(UserDetailsService::class.java)

        val passwordEncoder = BCryptPasswordEncoder()

        val user = User.withUsername("testUser")
            .password(passwordEncoder.encode("password"))
            .roles("USER")
            .build()

        // Configurar los mocks para devolver los valores que necesitas para tu prueba
        Mockito.`when`(request.getHeader("Authorization")).thenReturn("Bearer testToken")
        Mockito.`when`(jweService.extractEmail("testToken")).thenReturn("testUser")
        Mockito.`when`(userDetailsService.loadUserByUsername("testUser")).thenReturn(user)

        // Crear una instancia de JweAuthenticationFilter
        val jweAuthenticationFilter = object : JweAuthenticationFilter(jweService, userDetailsService) {
            public override fun doFilterInternal(
                request: HttpServletRequest,
                response: HttpServletResponse,
                filterChain: FilterChain
            ) {
                try {
                    val token = extractToken(request)

                    if (token != null) {
                        val username = jweService.extractEmail(token)
                        if (username != null && SecurityContextHolder.getContext().authentication == null) {
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
        }

        // Llamar al método doFilterInternal
        jweAuthenticationFilter.doFilterInternal(request, response, filterChain)
    }
}