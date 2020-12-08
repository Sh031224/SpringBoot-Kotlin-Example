package boot.spring.example.lib

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpServerErrorException
import java.lang.Exception
import javax.mail.internet.MimeMessage

@Component
class EmailSender {
    @Autowired
    private lateinit var javaMailSender: JavaMailSender

    @Value("\${spring.mail.username}")
    private lateinit var senderEmail: String

    fun sendEmail(email: String, title: String, content: String) {
        try {
            val mimeMessage: MimeMessage = javaMailSender.createMimeMessage()
            val helper: MimeMessageHelper = MimeMessageHelper(mimeMessage, false, "utf-8")
            helper.setTo(email)
            helper.setSubject(title)
            helper.setFrom(senderEmail)
            helper.setText(content, true)
            javaMailSender.send(mimeMessage)
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류.")
        }
    }
}