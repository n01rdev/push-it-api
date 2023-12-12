package com.nebrija.pushit.api.core.db.config

import com.nebrija.pushit.api.roles.infrastructure.db.postgres.entity.RoleEntity
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import com.nebrija.pushit.api.roles.infrastructure.db.postgres.repository.IRoleRepository

@Configuration
class InitDB {

    @Bean
    fun init(roleRepository: IRoleRepository): CommandLineRunner {
        return CommandLineRunner { _ ->
            if (roleRepository.findByName("User") == null) {
                roleRepository.save(RoleEntity(name = "User"))
            }
            if (roleRepository.findByName("Admin") == null) {
                roleRepository.save(RoleEntity(name = "Admin"))
            }
        }
    }
}