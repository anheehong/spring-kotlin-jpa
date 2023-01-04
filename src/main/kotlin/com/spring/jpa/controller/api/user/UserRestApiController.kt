package com.spring.jpa.controller.api.user

import com.spring.jpa.service.user.UserService
import com.spring.jpa.support.ApiReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserRestApiController(
    @Autowired val userService: UserService
) {

    @GetMapping("/")
    fun list( @RequestParam("search") search: String?, pageable: Pageable ) = ApiReturn.of(userService.list(search, pageable))

}