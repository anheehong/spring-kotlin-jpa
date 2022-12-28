package com.spring.jpa.controller.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class FrontController {
    companion object {
        var logger: Logger = LoggerFactory.getLogger( FrontController.javaClass )
    }

    @GetMapping("/home")
    fun home(): String {
        return "home"
    }

}