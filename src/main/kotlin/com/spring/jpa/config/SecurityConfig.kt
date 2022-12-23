package com.spring.jpa.config

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.format.DateTimeFormatter
import java.util.logging.Logger

@Configuration
@EnableWebSecurity
class SecurityConfig {

    companion object{
        const val URL_LOGIN = "/login"
        const val URL_API_LOGIN = "/ajax/login"
        const val URL_API_LOGOUT = "/ajax/logout"
        const val URL_LOGOUT = "/logout"
        const val URL_HOME = "/home"
    }
    
    @Bean
    fun filterChain( http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests()
            .requestMatchers( URL_API_LOGIN ).permitAll()
            .requestMatchers( URL_API_LOGOUT ).permitAll()
            .requestMatchers("/img/**", "/h2-console/**", "/icons/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .csrf().disable()

        return http.build()
    }

}

@Configuration
class ServerAppConfig : WebMvcConfigurer {

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd"
        const val TIME_FORMAT = "HH:mm:ss"
        const val DATE_TIME_FORMAT = "$DATE_FORMAT $TIME_FORMAT"

        var logger: Logger = Logger.getLogger( "ServerAppConfig" )
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