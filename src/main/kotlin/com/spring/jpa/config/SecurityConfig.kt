package com.spring.jpa.config

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.spring.jpa.authentication.CustomAuthenticationFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.format.DateTimeFormatter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Autowired val customAuthenticationFilter : CustomAuthenticationFilter
) {

    companion object{
        const val URL_API_LOGIN = "/auth/login"
        const val URL_API_LOGOUT = "/auth/logout"
        const val URL_LOGIN = "/login"
        const val URL_LOGOUT = "/logout"

        const val URL_HOME = "/home"

        const val LOGIN_ID = "username"
        const val LOGIN_PWD = "password"
    }
    
    @Bean
    fun filterChain( http: HttpSecurity): SecurityFilterChain {

        // 토큰 기반 인증이므로 세션 사용 안함
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore( customAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling()
            .defaultAuthenticationEntryPointFor(
                HttpStatusEntryPoint( HttpStatus.UNAUTHORIZED ),
                AntPathRequestMatcher( URL_API_LOGIN )
            )

        http
            .headers().frameOptions().sameOrigin()
            .and()
            .cors().and().csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers( URL_API_LOGIN ).permitAll()
            .anyRequest()
            .authenticated()

        http.formLogin()
            .loginProcessingUrl( URL_API_LOGIN )
            .defaultSuccessUrl( URL_HOME )
            .and()
            .logout()
            .logoutSuccessUrl( URL_LOGIN )

        return http.build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer? {
        return WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring()
                .requestMatchers( "/h2-console/**", "/img/**", "/resources/**" )
        }
    }



}

@Configuration
class ServerAppConfig : WebMvcConfigurer {

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd"
        const val TIME_FORMAT = "HH:mm:ss"
        const val DATE_TIME_FORMAT = "$DATE_FORMAT $TIME_FORMAT"

        var logger: Logger = LoggerFactory.getLogger( ServerAppConfig.javaClass )
    }

    fun onInit() {
        logger.info("Starting App ...")
    }

    fun onDestroy() {
        logger.info("shutdown..")
    }

    @Bean
    fun jsonCustomizer(): Jackson2ObjectMapperBuilderCustomizer = Jackson2ObjectMapperBuilderCustomizer {
        it.simpleDateFormat(DATE_FORMAT)
            .serializers(
                LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
            )
            .deserializers(
                LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
            )
    }

}