package boot.spring.example.lib

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.MessageDigest

@Component
class Encrypt {
    @Value("\${java.encrypt.key}")
    private val secretKey: String? = null

    fun sha256(content: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        md.update((content + secretKey).toByteArray())
        val builder = StringBuilder()
        for (b in md.digest()) {
            builder.append(String.format("%02x", b))
        }
        return builder.toString()
    }
}