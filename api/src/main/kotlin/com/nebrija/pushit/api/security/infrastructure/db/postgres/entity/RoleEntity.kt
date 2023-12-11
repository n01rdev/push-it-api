package com.nebrija.pushit.api.security.infrastructure.db.postgres.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "role")
data class RoleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
    private val id: Int,

    @Column(nullable = false, unique = true)
    private val uuid: UUID,

    @Column(nullable = false, unique = true)
    val name: String
)