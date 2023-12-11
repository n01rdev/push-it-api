package com.nebrija.pushit.api.security.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val authenticationProvider: AuthenticationProvider,
    private val jweAuthenticationFilter: JweAuthenticationFilter

) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf {
                it.
                    disable()
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/v1/security/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.
                sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jweAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}