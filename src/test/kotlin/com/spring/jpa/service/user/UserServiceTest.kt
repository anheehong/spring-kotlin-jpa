package com.spring.jpa.service.user

import com.spring.jpa.support.JwtTokenProvider
import com.spring.jpa.test.BaseTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.support.PageableUtils
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest(
    @Autowired val userService: UserService,
    @Autowired val passwordEncoder: PasswordEncoder,
    @Autowired val jwtTokenProvider: JwtTokenProvider
) : BaseTest() {

    @Test
    fun passwordEncoderTest(){
        val encodePwd = passwordEncoder.encode( "test" )
        assertNotNull( encodePwd )
    }

    @Test
    fun passwordMatchesTest(){
        val originPassword = "test"
        val encodePwd = passwordEncoder.encode( originPassword )
        assertNotNull( encodePwd )
        assertEquals(true, passwordEncoder.matches( originPassword, encodePwd ) )
    }

    @Test
    fun create(){
        val request = UserRequestDto( "name", "password" ).apply {
            displayName = "displayName"
        }

        val createUser = userService.create( request )
        assertEquals( "name", createUser.username )

        val findUser = userService.findByIdOrElseThrow( "name" )
        assertEquals( "name", findUser.username )
    }

    @Test
    fun update(){
        var request = UserRequestDto( "name", "password" ).apply {
            displayName = "displayName"
        }

        val createUser = userService.create( request )
        assertEquals( "name", createUser.username )

        request.password = "passwordUpdate"
        request.displayName = "displayNameUpdate"

        val updateUser = userService.update( "name", request )
        assertEquals( "displayNameUpdate", createUser.displayName)
    }

    @Test
    fun updateByTokenThrow(){

        val token = jwtTokenProvider.createAccessToken( "name" )

        assertThrows( UsernameNotFoundException::class.java ){
            val updateUser = userService.updateByToken( "name", token )
            println( updateUser.token )
        }
    }

    @Test
    fun updateByToken(){

        val token = jwtTokenProvider.createAccessToken( "admin" )
        val updateUser = userService.updateByToken( "admin", token )
        assertNotNull(updateUser.token)
    }


    @Test
    fun list(){

        val pageable = Pageable.ofSize( 10 )
        val search = null

        val list = userService.list( search, pageable )
        list.forEach{ l -> println( "${l.username} ${l.displayName} ${l.token}") }
    }
}