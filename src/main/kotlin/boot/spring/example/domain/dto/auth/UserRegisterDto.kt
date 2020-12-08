package boot.spring.example.domain.dto.auth

import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.validator.constraints.Length

import java.util.*
import javax.validation.constraints.*

class UserRegisterDto {
    @NotBlank
    @Email
    @Length(max = 50)
    val email: String? = null

    @NotBlank
    @Length(max = 64)
    val pw: String? = null

    @NotBlank
    @Length(max = 45)
    val name: String? = null

    @JsonFormat(pattern="yyyy-MM-dd")
    val birth: Date? = null
}
