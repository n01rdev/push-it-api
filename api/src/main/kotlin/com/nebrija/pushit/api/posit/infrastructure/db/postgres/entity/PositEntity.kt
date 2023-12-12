package com.nebrija.pushit.api.posit.infrastructure.db.postgres.entity

import com.nebrija.pushit.api.security.infrastructure.db.postgres.entity.SecurityEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "posit")
data class PositEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "posit_id_seq")
    private val id: Long = 0,

    @Column(nullable = false, unique = true)
    val uuid: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", referencedColumnName = "uuid", nullable = false)
    var author: SecurityEntity,

    @Column(nullable = false)
    var title: String = "",

    @Column(nullable = false)
    var content: String = "",

    @Column(nullable = false)
    private var likes: Int = 0,

    @Column(nullable = false)
    var active: Boolean = true,

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(nullable = false)
    private var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column
    var deletedAt: LocalDateTime? = null
)
