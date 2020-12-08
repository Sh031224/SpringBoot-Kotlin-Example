package boot.spring.example.domain.dto.auth

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

class UserEmailDto {
    @Email
    @NotBlank
    val email: String? = null
}