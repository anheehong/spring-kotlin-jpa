package com.spring.jpa.support

import com.spring.jpa.config.JwtConf
import com.spring.jpa.service.user.UserService
import io.jsonwebtoken.*
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class JwtTokenProvider(
    @Autowired val jwtConf: JwtConf,
    @Autowired private val userDetailsService: LoginUserDetailsService,
    @Autowired private val userService: UserService
) {

    companion object{
        var logger: Logger = LoggerFactory.getLogger( JwtTokenProvider.javaClass )
        val SIGNATURE_ALG: SignatureAlgorithm = SignatureAlgorithm.HS256
    }

    // 토큰검증
    fun validateToken(token: String): Boolean {
        try {
            getAllClaims( token )
            return true
        } catch ( exception: Exception) {
            when (exception) {
                is SignatureException -> {
                    logger.error("Invalid JWT signature")
                }
                is MalformedJwtException -> {
                    logger.error("Invalid JWT token")
                }
                is ExpiredJwtException -> {
                    logger.error("Expired JWT token")
                }
                is UnsupportedJwtException -> {
                    logger.error("Unsupported JWT token")
                }
                is IllegalArgumentException -> {
                    logger.error("JWT claims string is empty.")
                }
            }
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
        return request.getHeader(HttpHeaders.AUTHORIZATION )
    }

    fun createTokenSet( username: String ): Pair<String, String>{
       return Pair( createAccessToken( username ), issueRefreshToken( username ) )
    }

    fun createAccessToken( username: String ): String{
        return createToken( username, jwtConf.atExpiredTime)
    }
    fun createRefreshToken( username: String ): String{
        return createToken( username, jwtConf.rtExpiredTime)
    }
    private fun createToken( username: String, expiredTime: Int): String{

        val claims: Claims = Jwts.claims().setSubject( username )
        claims["username"] = username

        val issuedAt: Instant = Instant.now().truncatedTo(ChronoUnit.SECONDS)
        val expiration: Instant = issuedAt.plus( expiredTime.toLong(), ChronoUnit.DAYS)

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(issuedAt))
            .setExpiration(Date.from(expiration))
            .signWith( SIGNATURE_ALG , jwtConf.secretKey)
            .compact()
    }

    @Transactional
    fun reissueRefreshToken( refreshToken: String): String?{
        val authentication = getAuthentication(refreshToken)
        val findRefreshToken = userService.findByIdOrElseThrow(authentication.name)

        return if ( findRefreshToken.token == refreshToken ) {
            val newRefreshToken: String = createRefreshToken( authentication.name )
            userService.updateByToken( authentication.name, newRefreshToken )
            newRefreshToken
        } else {
            logger.info("refresh 토큰이 일치하지 않습니다. ")
            null
        }
    }

    @Transactional
    fun issueRefreshToken( username: String ): String {
        val newRefreshToken: String = createRefreshToken( username )
        userService.updateByToken( username, newRefreshToken )
        return newRefreshToken
    }

}