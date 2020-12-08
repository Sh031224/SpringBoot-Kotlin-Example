package boot.spring.example.domain.model.http

import org.springframework.http.HttpStatus

open class Response(status: HttpStatus, val message: String) {
    val status: Int = status.value()
}
