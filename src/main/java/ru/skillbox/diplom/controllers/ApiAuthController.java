package ru.skillbox.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.api.responses.CaptchaResponse;
import ru.skillbox.diplom.entities.CaptchaCode;
import ru.skillbox.diplom.services.CaptchaService;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("/")
public class ApiAuthController {
    @Autowired
    private CaptchaService captchaService;

    @GetMapping("api/auth/captcha")
    public CaptchaResponse genCaptcha() {
        CaptchaResponse captchaResponse = new CaptchaResponse();
        CaptchaCode captchaCode = captchaService.genAndSaveCaptcha();
        captchaResponse.setImage("data:image/png;base64," + captchaCode.getSecretCode());
        captchaResponse.setSecret(Base64.getEncoder().encodeToString(captchaCode.getCode().getBytes()));
        return captchaResponse;
    }
}