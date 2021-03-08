package boot.spring.example.lib

import org.apache.logging.log4j.util.Strings
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class AuthorizationExtractor {

    companion object {

        val AUTHORIZATION: String = "Authorization"

        fun extract(request: HttpServletRequest, type: String): String {
            val headers: Enumeration<String> = request.getHeaders(AUTHORIZATION)
            while (headers.hasMoreElements()) {
                val value: String = headers.nextElement()
                if (value.toLowerCase().startsWith(type.toLowerCase())) {
                    return value.substring(type.length).trim()
                }
            }

            return Strings.EMPTY
        }

    }
}