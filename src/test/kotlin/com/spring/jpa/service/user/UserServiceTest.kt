package com.spring.jpa.service.user

import com.spring.jpa.support.JwtTokenProvider
import com.spring.jpa.test.BaseTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest(
    @Autowired val userService: UserService,
    @Autowired val passwordEncoder: PasswordEncoder,
    @Autowired val jwtTokenProvider: JwtTokenProvider
) : BaseTest() {

    @Test
    fun passwordEncoderTest(){

        val encodePwd = passwordEncoder.encode( "test" )
        print( encodePwd )
    }

    @Test
    fun passwordMatchesTest(){

        val originPassword = "test"
        val encodePwd = passwordEncoder.encode( originPassword )
        println( encodePwd )
        println( passwordEncoder.matches( originPassword, encodePwd ) )
    }

    @Test
    fun create(){
        val request = UserRequestDto( "name", "password", "displayName")
        val createUser = userService.create( request )
        println( createUser.username )

        val findUser = userService.findById( "name" )
        println( findUser.username )
    }

    @Test
    fun update(){
        var request = UserRequestDto( "name", "password", "displayName")
        val createUser = userService.create( request )
        println( createUser.username )

        request.password = "passwordUpdate"
        request.displayName = "displayNameUpdate"

        val updateUser = userService.update( "name", request )
        println( createUser.displayName )
    }

    @Test
    fun updateByToken(){

        val token = jwtTokenProvider.createAccessToken( "name" )
        val updateUser = userService.updateByToken( "name", token )
        println( updateUser.token )
    }

}