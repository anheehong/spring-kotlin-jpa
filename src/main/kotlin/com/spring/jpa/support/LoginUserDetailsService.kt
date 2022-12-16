package com.spring.jpa.support

import com.spring.jpa.service.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


/**
 *
 * @author anakin
 * @date 2020/08/31
 */
@Service
class LoginUserDetailsService : UserDetailsService {
    @Autowired lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails = userRepository.findById(username!!).orElseThrow { Exception( "not exist") }
}
