package boot.spring.example.filter

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletResponse

@Component
@Order(1)
class CorsFilter: Filter {

    override fun doFilter(req: ServletRequest?, res: ServletResponse, chain: FilterChain) {
        val response = res as HttpServletResponse
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Credentials", "true")
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH, HEAD")
        response.setHeader("Access-Control-Max-Age", "-1")
        response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Request-Headers, Cache-Control, Pragma, Expires")
        response.setHeader("Access-Control-Expose-Headers", "content-disposition")

        chain.doFilter(req, res)
    }

    override fun init(filterConfig: FilterConfig?) {}
    override fun destroy() {}

}