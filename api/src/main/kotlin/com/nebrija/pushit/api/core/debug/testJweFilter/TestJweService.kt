package com.nebrija.pushit.api.core.debug.testJweFilter

import com.nebrija.pushit.api.security.application.service.JweService
import com.nebrija.pushit.api.security.config.JweAuthenticationFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService

class TestJweFilter {

    @Test
    fun testDoFilterInternal() {
        // Crear instancias simuladas de HttpServletRequest, HttpServletResponse y FilterChain
        val request = Mockito.mock(HttpServletRequest::class.java)
        val response = Mockito.mock(HttpServletResponse::class.java)
        val filterChain = Mockito.mock(FilterChain::class.java)

        // Crear instancias de JweService y UserDetailsService
        val jweService = Mockito.mock(JweService::class.java)
        val userDetailsService = Mockito.mock(UserDetailsService::class.java)

        // Generar un token utilizando JweService
        val username = "testUser"
        val claims = mapOf("email" to username)
        val generatedToken = jweService.generateToken(claims, username)
        println("Generated token: $generatedToken")

        // Configurar los mocks para devolver los valores que necesitas para tu prueba
        Mockito.`when`(request.getHeader("Authorization")).thenReturn("Bearer $generatedToken")
        println("token: $generatedToken")
        Mockito.`when`(jweService.extractEmail(generatedToken)).thenReturn(username)
        println("username: $username")
        Mockito.`when`(userDetailsService.loadUserByUsername(username)).thenReturn(User.withUsername(username).password("password").roles("USER").build())
        println("userDetails: ${userDetailsService.loadUserByUsername(username)}")

        // Crear una instancia de JweAuthenticationFilter
        val jweAuthenticationFilter = JweAuthenticationFilter(jweService, userDetailsService)
        println("jweAuthenticationFilter: $jweAuthenticationFilter")

        // Llamar al método doFilterInternal
        jweAuthenticationFilter.doFilterInternal(request, response, filterChain)
        println("jweAuthenticationFilter.doFilterInternal(request, response, filterChain): ${jweAuthenticationFilter.doFilterInternal(request, response, filterChain)}")
    }
}