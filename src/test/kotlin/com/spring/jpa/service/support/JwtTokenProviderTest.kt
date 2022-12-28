package com.spring.jpa.service.support

import com.spring.jpa.support.JwtTokenProvider
import com.spring.jpa.test.BaseTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

class JwtTokenProviderTest(
    @Autowired private val jwtTokenProvider: JwtTokenProvider
    ) : BaseTest() {

    @Test
    fun createToken(){
        val token = jwtTokenProvider.createToken( "test" )
        print( token )
    }

    @Test
    fun validation(){
        val token = jwtTokenProvider.createToken( "test" )
        print( jwtTokenProvider.validateToken( token ) )
    }

    @Test
    fun getUsername(){
        val token = jwtTokenProvider.createToken( "test" )
        print( jwtTokenProvider.getUsername( token ) )
    }


}