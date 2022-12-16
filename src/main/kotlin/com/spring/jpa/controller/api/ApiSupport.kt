package com.spring.jpa.controller.api

import com.spring.jpa.support.ApiReturn
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiSupportController.URL)
class ApiSupportController {

    @GetMapping("/version")
    fun version(): ApiReturn<String> {
        return ApiReturn.of(VERSION)
    }

    companion object{
        const val VERSION = "v1"
        const val URL = "/client-api/$VERSION"
    }
}
