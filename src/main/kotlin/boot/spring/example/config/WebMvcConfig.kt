package boot.spring.example.config

import boot.spring.example.interceptor.AuthInterceptor
import boot.spring.example.interceptor.OptionalAuthInterceptor
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebMvcConfig: WebMvcConfigurer{

    @Autowired
    lateinit var authInterceptor: AuthInterceptor

    @Autowired
    lateinit var optionalAuthInterceptor: OptionalAuthInterceptor

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/auth/changePw")
                .addPathPatterns("/user/*")

//        registry.addInterceptor(optionalAuthInterceptor)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("*")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowCredentials(false)
                .maxAge(3600)
    }
}
