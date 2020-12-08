package boot.spring.example.controller

import boot.spring.example.domain.dto.auth.*
import boot.spring.example.domain.model.http.Response
import boot.spring.example.domain.model.http.ResponseData
import boot.spring.example.domain.model.user.UserLoginRo
import boot.spring.example.domain.model.user.UserTokenRo
import boot.spring.example.enums.jwt.JwtAuth
import boot.spring.example.service.auth.AuthServiceImpl
import boot.spring.example.service.jwt.JwtServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import javax.validation.Valid


/**
 * 유저 인증
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin
class AuthController {
    @Autowired
    private lateinit var authService: AuthServiceImpl

    @Autowired
    private lateinit var jwtService: JwtServiceImpl

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    fun login(@RequestBody @Valid userLoginDto: UserLoginDto): ResponseData<UserLoginRo> {
        try {
            val userIdx: Long? = authService.login(userLoginDto)
            val accessToken: String = jwtService.createToken(userIdx!!, JwtAuth.ACCESS)
            val refreshToken: String = jwtService.createToken(userIdx, JwtAuth.REFRESH)
            val data: UserLoginRo = UserLoginRo(accessToken, refreshToken)
            return ResponseData(HttpStatus.OK, "로그인 성공.", data);
        } catch (e: HttpClientErrorException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류.")
        }
    }

    /**
     * 회원가입 API
     */
    @PostMapping("/register")
    fun register(@RequestBody @Valid userRegisterDto: UserRegisterDto): Response {
        try {
            authService.register(userRegisterDto)

            return Response(HttpStatus.OK, "가입성공")
        } catch (e: HttpClientErrorException) {
            throw e
        } catch (e: Exception) {
            println(e)
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류.")
        }
    }

    /**
     *  토큰 갱신
     */
    @PostMapping("/token")
    fun refresh(@RequestBody @Valid userTokenDto: UserTokenDto): ResponseData<UserTokenRo> {
        try {
            val accessToken: String? = jwtService.refreshToken(userTokenDto.refreshToken)
            val data: UserTokenRo = UserTokenRo(accessToken!!)
            return ResponseData(HttpStatus.OK, "토큰 갱신 성공.", data)
        } catch (e: HttpClientErrorException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류.")
        }
    }

    /**
     * 회원가입 이메일 코드 전송
     */
    @PostMapping("/authCode")
    fun sendAuthEmailCode(@RequestBody @Valid userEmailDto: UserEmailDto): Response {
        try {
            authService.sendAuthEmailCode(userEmailDto.email!!)
            return Response(HttpStatus.OK, "발송 성공.")
        } catch (e: HttpClientErrorException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류.")
        }
    }
}