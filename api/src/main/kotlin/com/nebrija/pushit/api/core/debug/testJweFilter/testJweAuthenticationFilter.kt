package com.nebrija.pushit.api.core.debug.testJweFilter

import com.nebrija.pushit.api.security.application.service.JweService
import com.nebrija.pushit.api.security.config.JweAuthenticationFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService


class TestJweAuthenticationFilter{

    @Test
    fun testJweAuthenticationFilter() {
        // Crear instancias simuladas de HttpServletRequest, HttpServletResponse y FilterChain
        val request = Mockito.mock(HttpServletRequest::class.java)
        val response = Mockito.mock(HttpServletResponse::class.java)
        val filterChain = Mockito.mock(FilterChain::class.java)

        // Crear instancias de JweService y UserDetailsService
        val jweService = Mockito.mock(JweService::class.java)
        val userDetailsService = Mockito.mock(UserDetailsService::class.java)

        // Generar un token y un nombre de usuario para la prueba
        val token = "testToken"
        val username = "testUser"

        // Configurar JweService para devolver el token y el nombre de usuario
        Mockito.`when`(request.getHeader("Authorization")).thenReturn("Bearer $token")
        Mockito.`when`(jweService.extractEmail("testToken")).thenReturn(username)

        // Configurar UserDetailsService para devolver un UserDetails
        val userDetails = User.withUsername(username).password("password").roles("USER").build()
        Mockito.`when`(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails)

        // Crear una instancia de JweAuthenticationFilter
        val jweAuthenticationFilter = JweAuthenticationFilter(jweService, userDetailsService)

        // Configurar HttpServletRequest para devolver un encabezado de autorización con el token
        Mockito.`when`(request.getHeader("Authorization")).thenReturn("Bearer $token")

        // Llamar al método doFilterInternal
        jweAuthenticationFilter.doFilterInternal(request, response, filterChain)
    }
}
