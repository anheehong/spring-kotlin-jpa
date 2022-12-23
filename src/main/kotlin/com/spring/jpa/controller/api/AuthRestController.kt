package com.spring.jpa.controller.api

import com.spring.jpa.support.ApiReturn
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/")
class AuthRestController {

    @GetMapping("/login")
    fun login(): ApiReturn<String> {

        print( "login" )
        return ApiReturn.of( "" )
    }
}