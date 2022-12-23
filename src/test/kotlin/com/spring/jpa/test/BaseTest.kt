package com.spring.jpa.test

import com.spring.jpa.SpringJpaApplication
import jakarta.persistence.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.annotation.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [SpringJpaApplication::class])
@ActiveProfiles(profiles = ["local"])
@Transactional
abstract class BaseTest {


}