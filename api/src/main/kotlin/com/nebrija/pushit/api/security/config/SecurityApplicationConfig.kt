package com.nebrija.pushit.api.security.config

import com.nebrija.pushit.api.security.infrastructure.db.postgres.repository.SecurityRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class SecurityApplicationConfig(private val repository: SecurityRepository) {

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl())
        authenticationProvider.setPasswordEncoder(passwordEncoder())
        authenticationProvider.setAuthoritiesMapper(authoritiesMapper())
        return authenticationProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager?: throw Exception("AuthenticationManager not found")
    }

    @Bean
    fun userDetailsServiceImpl(): UserDetailsService {
        return UserDetailsService { email ->
            repository.findByEmailEntity(email)?.let {
                User(
                    it.username,
                    it.password,
                    it.active,
                    it.active,
                    it.active,
                    it.active,
                    it.authorities.map { role -> SimpleGrantedAuthority(role.authority) }
                )
            } ?: throw UsernameNotFoundException("User not found with email: $email") //Info leak, just for demo purposes
        }
    }

    @Bean
    fun authoritiesMapper(): GrantedAuthoritiesMapper {
        val authoritiesMapper = SimpleAuthorityMapper()
        authoritiesMapper.setConvertToUpperCase(true)
        return authoritiesMapper
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

}
