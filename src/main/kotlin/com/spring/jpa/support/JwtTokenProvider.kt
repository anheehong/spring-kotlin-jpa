package com.spring.jpa.support

import com.spring.jpa.config.JwtConf
import io.jsonwebtoken.*
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class JwtTokenProvider(
    @Autowired val jwtConf: JwtConf,
    private val userDetailsService: UserDetailsService
) {

    companion object{
        var logger: Logger = LoggerFactory.getLogger( JwtTokenProvider.javaClass )
        val SIGNATURE_ALG: SignatureAlgorithm = SignatureAlgorithm.HS256
    }

    fun createToken( username: String ): String{
        val claims: Claims = Jwts.claims().setSubject( username )
        claims["username"] = username

        val issuedAt: Instant = Instant.now().truncatedTo(ChronoUnit.SECONDS)
        val expiration: Instant = issuedAt.plus( jwtConf.expiredTime.toLong(), ChronoUnit.DAYS)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(issuedAt))
            .setExpiration(Date.from(expiration))
            .signWith( SIGNATURE_ALG , jwtConf.secretKey)
            .compact()
    }

    // 토큰검증
    fun validateToken(token: String): Boolean {
        try {
            getAllClaims( token )
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }
        return false
    }

    // 모든 Claims 조회
    private fun getAllClaims(token: String): Claims {
        return Jwts.parser()
            .setSigningKey( jwtConf.secretKey )
            .parseClaimsJws(token)
            .body
    }

    // 토큰에서 username 파싱
    fun getUsername(token: String): String {
        val claims: Claims = getAllClaims(token)
        return claims["username"] as String
    }

    // JWT 토큰에서 인증 정보 조회
    fun getAuthentication(token: String): Authentication {
        val userDetails = userDetailsService.loadUserByUsername( getUsername(token) )
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    fun resolveToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")
    }

}