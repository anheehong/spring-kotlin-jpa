package com.spring.jpa.controller

import com.spring.jpa.config.SecurityConfig
import com.spring.jpa.service.user.UserRequestDto
import com.spring.jpa.test.MockTest
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

class JwtTokenRestControllerTest: MockTest() {


    @Test
    fun loginTest() {
        val request = UserRequestDto( "admin", "test" )
        val result = mvc.perform(
            RestDocumentationRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(request)).servletPath( "/auth/login" )
                .param( "username", "admin" )
                .param( "password", "test" )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.header().exists( "AT") )
            .andExpect(MockMvcResultMatchers.header().exists( "RT") )
            .andExpect(MockMvcResultMatchers.redirectedUrl( SecurityConfig.URL_HOME ) )

        val temp = result.andReturn()
        val accessToken = temp.response.getHeaderValue( "AT" )
        val refreshToken = temp.response.getHeaderValue( "RT" )

        println( "accessToken : $accessToken" )
        println( "refreshToken : $refreshToken" )
    }

    @Test
    fun apiTest401(){
        mvc.perform(RestDocumentationRequestBuilders.get("/api/user/"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isUnauthorized )
    }

    @Test
    fun apiTest200(){

        val request = UserRequestDto( "admin", "test" )
        val result = mvc.perform(
            RestDocumentationRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(request)).servletPath( "/auth/login" )
                .param( "username", "admin" )
                .param( "password", "test" )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.header().exists( "AT") )
            .andExpect(MockMvcResultMatchers.header().exists( "RT") )
            .andExpect(MockMvcResultMatchers.redirectedUrl( SecurityConfig.URL_HOME ) )

        val temp = result.andReturn()
        val accessToken = temp.response.getHeaderValue( "AT" )
        val refreshToken = temp.response.getHeaderValue( "RT" )

        println( "accessToken : $accessToken" )
        println( "refreshToken : $refreshToken" )

        mvc.perform(RestDocumentationRequestBuilders.get("/api/user/")
            .header( HttpHeaders.AUTHORIZATION, "Bearer $accessToken" )
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk )
    }
}