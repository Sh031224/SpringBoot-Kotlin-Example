package boot.spring.example.service.jwt

import io.jsonwebtoken.*
import boot.spring.example.constant.DateConstant
import boot.spring.example.domain.entity.User
import boot.spring.example.domain.repository.UserRepo
import boot.spring.example.enums.jwt.JwtAuth
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec
import io.jsonwebtoken.security.SignatureException


@Service
class JwtServiceImpl: JwtService {
    @Autowired
    private lateinit var userRepo: UserRepo

    @Value("\${jwt.secret.access}")
    private val secretAccessKey: String? = null

    @Value("\${jwt.secret.refresh}")
    private val secretRefreshKey: String? = null

    val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS256

    /**
     * 토큰 생성
     * @return Token
     */
    override fun createToken(idx: Long, authType: JwtAuth): String {
        var expiredAt: Date = Date()
        var secretKey: String? = null

        secretKey = when(authType) {
            JwtAuth.ACCESS -> {
                expiredAt = Date(expiredAt.time + DateConstant().MILLISECONDS_FOR_A_HOUR * 1)
                secretAccessKey
            }
            JwtAuth.REFRESH -> {
                expiredAt = Date(expiredAt.time + DateConstant().MILLISECONDS_FOR_A_HOUR * 24 * 7)
                secretRefreshKey
            }
            else -> {
                throw HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류.")
            }
        }

        val signInKey: Key = SecretKeySpec(secretKey!!.toByteArray(), signatureAlgorithm.jcaName)

        val headerMap: MutableMap<String, Any> = HashMap()

        headerMap["typ"] = "JWT"
        headerMap["alg"] = "HS256"

        val map: MutableMap<String, Any> = HashMap()

        map["idx"] = idx

        val builder: JwtBuilder = Jwts.builder().setHeaderParams(headerMap)
                .setClaims(map)
                .setExpiration(expiredAt)
                .signWith(signInKey)


        return builder.compact()
    }

    /**
     * 토큰 검증
     * @return User
     */
    override fun validateToken(token: String?): User? {
        try {
            val signatureAlgorithm: SignatureAlgorithm = SignatureAlgorithm.HS256

            val signingKey: Key = SecretKeySpec(secretAccessKey!!.toByteArray(), signatureAlgorithm.jcaName)
            val claims: Claims = Jwts.parserBuilder().setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .body

            return userRepo.findByIdx(claims["idx"].toString().toLong())
                    ?: throw HttpClientErrorException(HttpStatus.NOT_FOUND, "유저 없음.")

        } catch (e: ExpiredJwtException) {
            throw HttpClientErrorException(HttpStatus.GONE, "토큰 만료.")
        } catch (e: SignatureException) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰 위조.")
        } catch (e: MalformedJwtException) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰 위조.")
        } catch (e: IllegalArgumentException) {
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "토큰 없음.")
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류.")
        }
    }

    /**
     * 토큰 갱신
     * @return refresh token
     */
    override fun refreshToken(refreshToken: String?): String? {
        try {
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "검증 오류.")
            }

            val signingKey: Key = SecretKeySpec(secretRefreshKey!!.toByteArray(), signatureAlgorithm.jcaName)
            val claims: Claims = Jwts.parserBuilder().setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .body

            val user: User = userRepo.findByIdx(claims["idx"].toString().toLong())
                    ?: throw HttpClientErrorException(HttpStatus.NOT_FOUND, "유저 없음.")

            return createToken(user.idx!!, JwtAuth.ACCESS)
        } catch (e: ExpiredJwtException) {
            throw HttpClientErrorException(HttpStatus.GONE, "토큰 만료.")
        } catch (e: SignatureException) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰 위조.")
        } catch (e: MalformedJwtException) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "토큰 위조.")
        } catch (e: IllegalArgumentException) {
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "토큰 없음.")
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류.")
        }
    }
}