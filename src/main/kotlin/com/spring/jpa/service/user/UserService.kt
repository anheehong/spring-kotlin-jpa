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

    fun findById( id: String ) = userRepository.findById( id )

    fun create( request: UserRequestDto )= with( userRepository ) {
        saveAndFlush(User().updateBy(request)).dto
    }
    fun update( id: String, request: UserRequestDto ) = with( userRepository ){
        findById( id ).get().updateByMe( request ).let {
            saveAndFlush( it )
        }.dto
    }

    fun updateByToken( id: String, token: String ) = with( userRepository ){
        findById( id ).get().apply {
            this.token = token
        }.let {
            saveAndFlush( it )
        }.dto
    }

    private val UserRequestDto.entity get() = User().updateBy( this )
    private fun User.updateBy( request : UserRequestDto ) = this.apply{
        _username = request.username
        if( request.password != "" ){
            _password = passwordEncoder.encode(request.password)
        }
        displayName = request.displayName
        token = request.token
    }

    private fun User.updateByMe( request : UserRequestDto ) = this.apply{
        if( request.password != "" ){
            _password = passwordEncoder.encode(request.password)
        }
        displayName = request.displayName
    }
}



interface UserRepository : JpaRepository<User, String>, QuerydslPredicateExecutor<User>{


}