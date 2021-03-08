package boot.spring.example.filter

import boot.spring.example.domain.entity.User
import boot.spring.example.lib.AuthorizationExtractor
import boot.spring.example.service.jwt.JwtServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.support.WebApplicationContextUtils
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(private val handlerExceptionResolver: HandlerExceptionResolver): Filter {

    @Autowired
    private lateinit var jwtServiceImpl: JwtServiceImpl

    override fun init(cfg: FilterConfig) {
        val ctx: ApplicationContext = WebApplicationContextUtils
                .getRequiredWebApplicationContext(cfg.servletContext)
        this.jwtServiceImpl = ctx.getBean(JwtServiceImpl::class.java)
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        try {
            val token: String = AuthorizationExtractor.extract(request as HttpServletRequest, "Bearer")

            // cors
            if (request.method != "OPTIONS") {
                if (StringUtils.isEmpty(token)) {
                    throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "검증 오류.")
                }

                val user: User? = jwtServiceImpl.validateToken(token)

                request.setAttribute("user", user)
            }

            chain.doFilter(request, response)
        } catch (e: Exception) {
            handlerExceptionResolver.resolveException(request as HttpServletRequest, response as HttpServletResponse, null, e)
        }
    }
}