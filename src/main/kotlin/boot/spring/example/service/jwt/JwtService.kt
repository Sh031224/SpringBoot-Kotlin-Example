package boot.spring.example.service.jwt

import boot.spring.example.domain.entity.User
import boot.spring.example.enums.jwt.JwtAuth

interface JwtService {
    fun createToken(idx: Long, authType: JwtAuth): String
    fun validateToken(token: String?): User?
    fun refreshToken(refreshToken: String?): String?
}