package com.spring.jpa.authentication

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class CustomAuthenticationFilter() : UsernamePasswordAuthenticationFilter() {

    constructor( authenticationManager: AuthenticationManager ): this(){
        super.setAuthenticationManager( authenticationManager )
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse?): Authentication? {

        val username = this.obtainUsername( request )?.trim()
        val password = this.obtainPassword( request )?.trim()

        val authRequest = UsernamePasswordAuthenticationToken( username, password )
        this.setDetails(request, authRequest)

        return authenticationManager.authenticate(authRequest)
    }
}