package com.spring.jpa.service.user

import com.spring.jpa.test.BaseTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

class UserServiceTest(
    @Autowired userService: UserService,
    @Autowired val passwordEncoder: PasswordEncoder
) : BaseTest() {

    @Test
    fun passwordEncoderTest(){

        val encodePwd = passwordEncoder.encode( "test" )
        print( encodePwd )
    }

}