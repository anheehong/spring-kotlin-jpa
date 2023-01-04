package com.spring.jpa.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.spring.jpa.SpringJpaApplication
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpSession
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter
import java.util.*

@ExtendWith(SpringExtension::class, RestDocumentationExtension::class)
@SpringBootTest(classes = [SpringJpaApplication::class])
@ActiveProfiles(profiles = ["local"])
@WebAppConfiguration
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs // rest docs 자동 설정
abstract class MockTest {

    protected lateinit var mvc: MockMvc
    protected lateinit var httpSession: MockHttpSession

    @Autowired protected lateinit var objectMapper: ObjectMapper

    protected fun asJson(any: Any): String = objectMapper.writeValueAsString(any)

    @BeforeEach
    fun before(ctx: WebApplicationContext, restDocs: RestDocumentationContextProvider) {
        MockitoAnnotations.openMocks(this)
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
            .addFilters<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .apply<DefaultMockMvcBuilder>(SecurityMockMvcConfigurers.springSecurity())
            .apply<DefaultMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(restDocs).operationPreprocessors().withRequestDefaults(
                    Preprocessors.removeHeaders("X-RNR-Authorization")))
            .build()
        httpSession = MockHttpSession(ctx.servletContext, UUID.randomUUID().toString())
    }
}