package com.spring.jpa.service.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService (
    @Autowired val userRepository: UserRepository,
    @Autowired val passwordEncoder: PasswordEncoder
){

    fun findById( id: String ) = userRepository.findById( id ).orElseThrow { Exception( "not exist") }

    private val UserRequestDto.entity get() = User().updateBy( this )
    private fun User.updateBy( request : UserRequestDto ) = this.apply{
        _password = passwordEncoder.encode(request.password)
    }
}



interface UserRepository : JpaRepository<User, String>, QuerydslPredicateExecutor<User>