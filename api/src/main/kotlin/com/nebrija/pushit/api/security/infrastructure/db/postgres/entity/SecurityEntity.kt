package com.nebrija.pushit.api.security.infrastructure.db.postgres.entity

import com.nebrija.pushit.api.roles.infrastructure.db.postgres.entity.RoleEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "security")
data class SecurityEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "security_id_seq")
    private val id: Long = 0,

    @Column(nullable = false, unique = true)
    val uuid: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    private var password: String,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "security_role",
        joinColumns = [JoinColumn(name = "security_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")])
    var roles: Set<RoleEntity> = HashSet(),

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
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it.name) }.toMutableList()
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return active
    }

    override fun isAccountNonLocked(): Boolean {
        return active
    }

    override fun isCredentialsNonExpired(): Boolean {
        return active
    }

    override fun isEnabled(): Boolean {
        return active
    }
}
