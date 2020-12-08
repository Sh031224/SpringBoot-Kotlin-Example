package boot.spring.example.domain.repository

import boot.spring.example.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepo: JpaRepository<User, String> {
     fun findByEmailAndPw(email: String?, pw: String?): User?
     fun findByIdx(idx: Long?): User?
     fun findByEmail(email: String?): User?
}
