package ru.skillbox.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.api.reguests.Register;
import ru.skillbox.diplom.api.responses.CaptchaResponse;
import ru.skillbox.diplom.entities.CaptchaCode;
import ru.skillbox.diplom.services.CaptchaService;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ApiAuthController {

    private String code = "";

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("api/auth/captcha")
    public CaptchaResponse genCaptcha() {
        CaptchaResponse captchaResponse = new CaptchaResponse();
        CaptchaCode captchaCode = captchaService.genAndSaveCaptcha();
        captchaResponse.setImage("data:image/png;base64," + captchaCode.getSecretCode());
        captchaResponse.setSecret(Base64.getEncoder().encodeToString(captchaCode.getCode().getBytes()));
        code = Base64.getEncoder().encodeToString(captchaCode.getCode().getBytes());
        return captchaResponse;
    }

    @PostMapping("api/auth/register")
    public void test(){
        System.out.println("api/auth/register");
    }

//    @PostMapping("api/auth/register")
//    public Map<String, Boolean> testRegistr(@RequestBody Register register) {
//        System.out.println("email - " + register.getEmail());
//        System.out.println("captcha - " + register.getCaptcha());
//        if (register.getCaptcha_secret().equals(code)) System.out.println("РАВНЫ");
//        HashMap<String, Boolean> temp = new HashMap<>();
//        temp.put("result", true);
//        return temp;
//
//    }
}