package boot.spring.example.interceptor

import boot.spring.example.domain.entity.User
import boot.spring.example.lib.AuthorizationExtractor
import boot.spring.example.service.jwt.JwtServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OptionalAuthInterceptor: HandlerInterceptor {
    @Autowired
    lateinit var authExtractor: AuthorizationExtractor

    @Autowired
    lateinit var jwtServiceImpl: JwtServiceImpl

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token: String = authExtractor.extract(request, "Bearer")
        
        if (request.method == "OPTIONS") {
            // for cors
            return true
        }

        if (StringUtils.isEmpty(token)) {
            return true
        }

        val user: User? = jwtServiceImpl.validateToken(token)

        request.setAttribute("user", user)

        return true
    }
}
