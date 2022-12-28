package com.spring.jpa.config

import com.spring.jpa.authentication.CustomAuthenticationFailureHandler
import com.spring.jpa.authentication.CustomAuthenticationFilter
import com.spring.jpa.authentication.CustomAuthenticationProvider
import com.spring.jpa.authentication.CustomAuthenticationSuccessHandler
import com.spring.jpa.support.LoginUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class BaseConfig(
    @Autowired private val userDetailsService : LoginUserDetailsService,
    @Autowired private val customAuthenticationSuccessHandler: CustomAuthenticationSuccessHandler,
    @Autowired private val customAuthenticationFailureHandler: CustomAuthenticationFailureHandler,
) {

    @Bean
    fun delegatingPasswordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return ProviderManager( customAuthenticationProvider() )
    }

    @Bean
    fun customAuthenticationProvider() : CustomAuthenticationProvider{
        return CustomAuthenticationProvider( userDetailsService, delegatingPasswordEncoder() )
    }

    @Bean
    fun customAuthenticationFilter(): CustomAuthenticationFilter? {
        val customAuthenticationFilter = CustomAuthenticationFilter( authenticationManager() )
        customAuthenticationFilter.setFilterProcessesUrl(SecurityConfig.URL_API_LOGIN) // 접근 URL
        customAuthenticationFilter.setAuthenticationSuccessHandler( customAuthenticationSuccessHandler ) // '인증' 성공 시 해당 핸들러로 처리를 전가한다.
        customAuthenticationFilter.setAuthenticationFailureHandler( customAuthenticationFailureHandler ) // '인증' 실패 시 해당 핸들러로 처리를 전가한다.
        customAuthenticationFilter.afterPropertiesSet()
        return customAuthenticationFilter
    }
}

