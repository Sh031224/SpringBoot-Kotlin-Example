package boot.spring.example.controller;

import boot.spring.example.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@CrossOrigin
@Controller
class TemplateController {

    @Autowired
    private lateinit var authService: AuthService

    /**
     * 회원가입 이메일 코드 인증
     */
    @GetMapping("authConfirm")
    fun authEmailConfirm(@RequestParam("code") code: String, model: Model): String {
        try {
            val isExist: Boolean = authService.authEmailConfirm(code)
            model.addAttribute("isConfirm", isExist)
            return "authConfirm"
        } catch (e: HttpClientErrorException) {
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            throw HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류.")
        }
    }
}
