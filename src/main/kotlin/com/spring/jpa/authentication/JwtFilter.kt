package com.spring.jpa.authentication

import com.spring.jpa.config.SecurityConfig
import com.spring.jpa.support.JwtTokenProvider
import com.spring.jpa.support.LoginUserDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    @Autowired private val jwtTokenProvider: JwtTokenProvider,
    @Autowired private val userDetailsService: LoginUserDetailsService,
) : OncePerRequestFilter() {


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val servletPath = request.servletPath
        val authorizationHeader = jwtTokenProvider.resolveToken( request )

        if( servletPath == SecurityConfig.URL_API_LOGIN ){
            filterChain.doFilter( request, response)
        }
        else if (authorizationHeader == null ){

            response.status = SC_BAD_REQUEST
            response.contentType = APPLICATION_JSON_VALUE
            response.characterEncoding = "utf-8"
        }
        else{

            var token: String = authorizationHeader.substring(7)
            var userName: String = jwtTokenProvider.getUsername(token)

            if (userName != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(userName)
                if ( jwtTokenProvider.validateToken(token)) {
                    SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities ).apply {
                        details = WebAuthenticationDetailsSource().buildDetails( request )
                    }
                }
            }
            filterChain.doFilter( request, response)
        }

    }

}