package boot.spring.example.service.auth

import boot.spring.example.constant.DateConstant
import boot.spring.example.domain.dto.auth.UserLoginDto
import boot.spring.example.domain.dto.auth.UserRegisterDto
import boot.spring.example.domain.entity.*
import boot.spring.example.domain.repository.*
import boot.spring.example.lib.EmailSender
import boot.spring.example.lib.Encrypt
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import java.util.*


@Service
class AuthServiceImpl: AuthService {
    @Autowired
    private lateinit var userRepo: UserRepo

    @Autowired
    private lateinit var emailAuthRepo: EmailAuthRepo

    @Autowired
    private lateinit var emailSender: EmailSender

    @Value("\${server.url}")
    private lateinit var serverUrl: String

    /**
     * 로그인
     */
    override fun login(userLoginDto: UserLoginDto): Long? {
        val user: User  = userRepo.findByEmailAndPw(userLoginDto.email, userLoginDto.pw)
                ?: throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "인증 실패.")
        return user.idx
    }

    /**
     * 회원 가입
     */
    override fun register(userRegisterDto: UserRegisterDto) {
        checkEmail(userRegisterDto.email)
        val isExist: EmailAuth? = emailAuthRepo.findByEmail(userRegisterDto.email!!)
        if (isExist == null || !isExist.isCertified) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED, "인증 안된 이메일.")
        }

        isExist.code = null
        emailAuthRepo.save(isExist)

        val modelMapper = ModelMapper()
        val userMapped: User = modelMapper.map(userRegisterDto, User::class.java)
        userRepo.save(userMapped)
    }

    /**
     * 이메일 중복 확인
     */
    override fun checkEmail(email: String?) {
        if (email == null || email.trim().isEmpty()) {
            throw HttpClientErrorException(HttpStatus.BAD_REQUEST, "검증 오류.")
        }
        val user: User? = userRepo.findByEmail(email)
        if (user != null) {
            throw HttpClientErrorException(HttpStatus.CONFLICT, "중복된 이메일.")
        }
    }

    /**
     * 회원가입 이메일 인증 발송
     */
    override fun sendAuthEmailCode(email: String) {
        try {
            checkEmail(email)
            var emailAuth: EmailAuth? = emailAuthRepo.findByEmail(email)
            val expireAt: Date = Date()

            if (emailAuth == null) {
                emailAuth = EmailAuth()
            }

            val code: String = Encrypt().sha256(email + expireAt.toString())
            val href: String = "$serverUrl/authConfirm?code=$code"

            sendEmailAuth(email, href)

            expireAt.time += DateConstant().MILLISECONDS_FOR_A_MINUTE * 5
            emailAuth.isCertified = false
            emailAuth.expireAt = expireAt
            emailAuth.email = email
            emailAuth.code = code
            emailAuthRepo.save(emailAuth)
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * 회원가입 이메일 코드 검증
     */
    override fun authEmailConfirm(code: String?): Boolean {
        if (code == null) {
            return false
        }

        val emailAuth: EmailAuth? = emailAuthRepo.findByCode(code)

        if (emailAuth == null || emailAuth.expireAt!!.time < Date().time) {
            return false
        }

        emailAuth.isCertified = true
        emailAuth.code = null

        emailAuthRepo.save(emailAuth)
        return true
    }

    /**
     * 이메일 인증 발송
     */
    override fun sendEmailAuth(email: String, href: String) {
        emailSender.sendEmail(email, "이메일 본인 인증",
                StringBuffer("<link href=\"https://fonts.googleapis.com/css?family=Noto+Sans+KR:900&display=swap\" rel=\"stylesheet\">")
                        .append("<div style=\"")
                        .append("text-align: center; color: rgba(0,0,0,0.75); padding-bottom: 2%; margin: 0 auto; font-family: 'Noto Sans KR'; font-weight: 200;\">")
                        .append("<h3 style=\"font-size: 300%; border-bottom: 1px solid #eeeeee\">이메일 인증</h2>")
                        .append("<a href=\"").append(href)
                        .append("\" target='blank'>")
                        .append("<div style=\"border: solid 3px #000000; width: 30%; margin: 0 auto; font-size: 2rem; color: rgba(0,0,0);\">")
                        .append("인증 하기")
                        .append("</div>")
                        .append("</a></div>")
                        .append("<p style=\"font-size: 0.8rem;\">본인의 활동이 아닌 경우 본 메일을 무시해주세요.</p>").toString())
    }
}