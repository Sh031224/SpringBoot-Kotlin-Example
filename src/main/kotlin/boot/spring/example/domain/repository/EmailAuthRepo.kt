package boot.spring.example.domain.repository

import boot.spring.example.domain.entity.EmailAuth
import org.springframework.data.jpa.repository.JpaRepository

interface EmailAuthRepo: JpaRepository<EmailAuth, String> {
    fun findByEmail(email: String): EmailAuth?
    fun findByCode(code: String): EmailAuth?
}