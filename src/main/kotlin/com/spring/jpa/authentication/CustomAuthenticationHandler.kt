package com.spring.jpa.authentication

import com.spring.jpa.config.SecurityConfig
import com.spring.jpa.support.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationSuccessHandler(
    @Autowired private val jwtTokenProvider: JwtTokenProvider
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val jwt = jwtTokenProvider.createTokenSet( authentication.name )

        response.contentType = APPLICATION_JSON_VALUE
        response.characterEncoding = "utf-8"
        response.setHeader( "AT", jwt.first )
        response.setHeader( "RT", jwt.second )
        response.sendRedirect( SecurityConfig.URL_HOME )
    }
}

@Component
class CustomAuthenticationFailureHandler : AuthenticationFailureHandler {

    companion object {
        var logger: Logger = LoggerFactory.getLogger( CustomAuthenticationFailureHandler.javaClass )
    }

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {

        var errorMsg = ""

        when( exception ){
            is BadCredentialsException -> {
                errorMsg = "???????????? ??????????????? ?????? ????????????. ?????? ????????? ?????????."
            }
            is DisabledException -> {
                errorMsg = "????????? ???????????? ???????????????. ??????????????? ???????????????."
            }
            is LockedException -> {
                errorMsg = "???????????? ???????????? ???????????????. ???????????? ????????? ?????????."
            }
            is UsernameNotFoundException -> {
                errorMsg = ""
            }
            else -> {
                errorMsg = "???????????? ????????? ???????????? ?????????????????????."
            }
        }

        logger.error( errorMsg );

        request.setAttribute("errorMsg", errorMsg);
        request.getRequestDispatcher( "/error").forward(request, response);

    }
}