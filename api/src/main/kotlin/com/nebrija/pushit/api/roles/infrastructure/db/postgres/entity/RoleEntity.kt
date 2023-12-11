package com.nebrija.pushit.api.roles.infrastructure.db.postgres.entity

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "role")
data class RoleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
    val id: Int = 0,

    @Column(nullable = false, unique = true)
    val uuid: String = UUID.randomUUID().toString(),

    @Column(nullable = false, unique = true)
    val name: String
)