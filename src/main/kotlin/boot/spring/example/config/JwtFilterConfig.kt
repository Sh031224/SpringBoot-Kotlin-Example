package boot.spring.example.config

import boot.spring.example.filter.JwtAuthenticationFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
class JwtFilterConfig(private val handlerExceptionResolver: HandlerExceptionResolver) {

    @Bean
    fun authFilter(): FilterRegistrationBean<JwtAuthenticationFilter> {
        val registrationBean: FilterRegistrationBean<JwtAuthenticationFilter> = FilterRegistrationBean<JwtAuthenticationFilter>()
        registrationBean.filter = JwtAuthenticationFilter(handlerExceptionResolver)
//        registrationBean.addUrlPatterns("/example/example")
        registrationBean.order = 2

        return registrationBean
    }

}