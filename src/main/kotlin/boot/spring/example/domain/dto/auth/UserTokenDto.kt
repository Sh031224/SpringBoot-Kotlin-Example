package boot.spring.example.domain.dto.auth

import javax.validation.constraints.NotBlank

class UserTokenDto {
    @NotBlank
    val refreshToken: String? = null
}