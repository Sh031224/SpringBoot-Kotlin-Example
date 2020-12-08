package boot.spring.example.service.auth

import boot.spring.example.domain.dto.auth.UserLoginDto
import boot.spring.example.domain.dto.auth.UserRegisterDto

interface AuthService {
    fun login(userLoginDto: UserLoginDto): Long?
    fun register(userRegisterDto: UserRegisterDto)
    fun checkEmail(email: String?)
    fun sendAuthEmailCode(email: String)
    fun authEmailConfirm(code: String?): Boolean
    fun sendEmailAuth(email: String, href: String)
}