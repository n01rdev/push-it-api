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
        val jweLogger = org.slf4j.LoggerFactory.getLogger(UserDetailsService::class.java)
        return UserDetailsService { email ->
            repository.findByEmailEntity(email)?.let { userEntity ->
                jweLogger.info("User found with email: $email")
                val username = userEntity.username
                val password = userEntity.password
                val isAccountNonExpired = userEntity.isAccountNonExpired
                val isAccountNonLocked = userEntity.isAccountNonLocked
                val isCredentialsNonExpired = userEntity.isCredentialsNonExpired
                val isEnabled = userEntity.isEnabled
                val authorities = userEntity.authorities.map { role ->
                    val authority = role.authority ?: throw IllegalArgumentException("Authority cannot be null")
                    SimpleGrantedAuthority(authority)
                }
                User.builder()
                    .username(username)
                    .password(password)
                    .disabled(!isEnabled)
                    .accountExpired(!isAccountNonExpired)
                    .accountLocked(!isAccountNonLocked)
                    .credentialsExpired(!isCredentialsNonExpired)
                    .authorities(authorities)
                    .build()
            } ?: jweLogger.error("User not found with email: $email")
                throw UsernameNotFoundException("User not found with email: $email")
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
