package com.spring.jpa.authentication

import com.spring.jpa.support.LoginUserDetailsService
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder

class CustomAuthenticationProvider(
    private val userDetailsService: LoginUserDetailsService,
    private val passwordEncoder: PasswordEncoder,
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {

        val username = authentication.name
        val password = authentication.credentials as String

        val userDetails = userDetailsService.loadUserByUsername( username )

        return when(passwordEncoder.matches( password, userDetails.password)){
            true -> UsernamePasswordAuthenticationToken( userDetails, password, userDetails.authorities);
            false -> throw BadCredentialsException("Bad Credential")
        }
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return true
    }
}