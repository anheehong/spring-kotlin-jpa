package com.spring.jpa.service.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

class UserService {
}

interface UserRepository : JpaRepository<User, String>, QuerydslPredicateExecutor<User>