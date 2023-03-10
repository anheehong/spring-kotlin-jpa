package com.spring.jpa.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

class PropertyConfig {


}


@Configuration
@ConfigurationProperties(prefix = "jwt.conf", ignoreUnknownFields = false, ignoreInvalidFields = false)
class JwtConf() {
    var secretKey: String = ""
    var atExpiredTime: Int = 0
    var rtExpiredTime: Int = 0
}