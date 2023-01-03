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
                errorMsg = "아이디나 비밀번호가 맞지 않습니다. 다시 확인해 주세요."
            }
            is DisabledException -> {
                errorMsg = "계정이 비활성화 되었습니다. 관리자에게 문의하세요."
            }
            is LockedException -> {
                errorMsg = "이메일이 인증되지 않았습니다. 이메일을 확인해 주세요."
            }
            is UsernameNotFoundException -> {
                errorMsg = ""
            }
            else -> {
                errorMsg = "알수없는 이유로 로그인에 실패하였습니다."
            }
        }

        logger.error( errorMsg );

        request.setAttribute("errorMsg", errorMsg);
        request.getRequestDispatcher( "/error").forward(request, response);

    }
}