package boot.spring.example.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "email_auth")
class EmailAuth {
    // 순서
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idx: Long? = null

    // 이메일
    @Column(nullable = false, unique = true)
    var email: String? = null

    // 만료 시간
    @Column(nullable = false)
    var expireAt: Date? = null

    // 인증 코드
    @Column(nullable = true)
    var code: String? = null

    // 인증 여부
    @Column(nullable = false)
    var isCertified: Boolean = false
}