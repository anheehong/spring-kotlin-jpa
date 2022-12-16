package com.spring.jpa.service.user

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "TB_USER")
class User: UserDetails {

    @Id
    @Column(length = 50, name = "user_name")
    private lateinit var _username:String

    @Column(length = 120, nullable = false)
    var displayName: String = ""

    @Column(length = 120, name = "user_password", nullable = false)
    private lateinit var _password:String

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    val dtCreate: LocalDateTime? = null

    @UpdateTimestamp
    val dtUpdate: LocalDateTime? = null

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("Not yet implemented")
    }

    override fun getPassword(): String {
        TODO("Not yet implemented")
    }

    override fun getUsername(): String {
        TODO("Not yet implemented")
    }

    override fun isAccountNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAccountNonLocked(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCredentialsNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEnabled(): Boolean {
        TODO("Not yet implemented")
    }
}