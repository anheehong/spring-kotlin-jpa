package com.spring.jpa.service.user

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime


@Entity
@Table(name = "TB_USER")
class User: UserDetails {

    @Id
    @Column(length = 50, name = "user_name")
    lateinit var _username: String

    @Column(length = 120, name = "user_password", nullable = false)
    lateinit var _password: String

    @Column(length = 120, name = "display_name", nullable = false)
    var displayName: String = ""

    @Column(length = 1000, name = "token")
    var token: String = ""

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    val dtCreate: LocalDateTime? = null

    @UpdateTimestamp
    val dtUpdate: LocalDateTime? = null


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authority = SimpleGrantedAuthority("SUPER")
        val authorities: MutableCollection<GrantedAuthority> = ArrayList()
        authorities.add(authority)

        return authorities
    }

    override fun getPassword(): String {
        return _password
    }

    override fun getUsername(): String {
        return _username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}

