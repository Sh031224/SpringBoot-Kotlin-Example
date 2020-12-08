package boot.spring.example.interceptor

import boot.spring.example.domain.entity.User
import boot.spring.example.lib.AuthorizationExtractor
import boot.spring.example.service.jwt.JwtServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthInterceptor: HandlerInterceptor {

    @Autowired
    lateinit var authExtractor: AuthorizationExtractor

    @Autowired
    lateinit var jwtServiceImpl: JwtServiceImpl

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token: String = authExtractor.extract(request, "Bearer")

        if (StringUtils.isEmpty(token)) {
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "검증 오류.")
        } else if (request.method == "OPTIONS") {
            // for cors
            return true
        }

        val user: User? = jwtServiceImpl.validateToken(token)

        request.setAttribute("user", user)

        return true
    }
}