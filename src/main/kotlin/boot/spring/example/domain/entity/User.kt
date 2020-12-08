package boot.spring.example.domain.entity

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "user")
class User {

    // 순서
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idx: Long? = null

    // 이메일
    @Column(unique = true, nullable = false, length = 50)
    var email: String? = null

    // 이름
    @Column(nullable = false, length = 45)
    var name: String? = null

    // 비밀번호
    @Column(nullable = false, length = 64)
    var pw: String? = null

    // 생년월일
    @Column()
    var birth: Date? = Date()

    // 가입 날짜
    @CreationTimestamp()
    @Column(nullable = false)
    var createdAt: Date = Date()
}