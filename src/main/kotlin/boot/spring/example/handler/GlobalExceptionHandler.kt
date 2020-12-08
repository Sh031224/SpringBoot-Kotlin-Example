package boot.spring.example.handler

import boot.spring.example.domain.model.http.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException


@ControllerAdvice
 class GlobalExceptionHandler {
    @ExceptionHandler(HttpClientErrorException::class)
    fun handleHttpClientErrorException(e: HttpClientErrorException): ResponseEntity<Response> {
        val data = Response(e.statusCode, e.message!!)
        return ResponseEntity<Response>(data, e.statusCode)
    }

    @ExceptionHandler(HttpServerErrorException::class)
    fun handleHttpServerErrorException(e: HttpServerErrorException): ResponseEntity<Response> {
        val data = Response(e.statusCode, e.message!!)
        return ResponseEntity<Response>(data, e.statusCode)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<Response> {
        val data = Response(HttpStatus.BAD_REQUEST, "검증 오류.")
        return ResponseEntity<Response>(data, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(WebExchangeBindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun handleWebExchangeBindException(e: WebExchangeBindException): ResponseEntity<Response> {
        val data = Response(HttpStatus.BAD_REQUEST, "검증 오류.")
        return ResponseEntity<Response>(data, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<Response> {
        val data = Response(HttpStatus.BAD_REQUEST, "검증 오류.")
        return ResponseEntity<Response>(data, HttpStatus.BAD_REQUEST)
    }
}