package boot.spring.example.domain.model.http

import org.springframework.http.HttpStatus

class ResponseData<T>(status: HttpStatus, message: String, val data: T): Response(status, message)
