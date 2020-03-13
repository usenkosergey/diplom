package ru.skillbox.diplom.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.Mapper.UserMapper;
import ru.skillbox.diplom.api.requests.Login;
import ru.skillbox.diplom.api.requests.Register;
import ru.skillbox.diplom.api.responses.CaptchaResponse;
import ru.skillbox.diplom.api.responses.ResponseAll;
import ru.skillbox.diplom.api.responses.UserResponseAuth;
import ru.skillbox.diplom.entities.CaptchaCode;
import ru.skillbox.diplom.entities.User;
import ru.skillbox.diplom.repositories.CaptchaRepositori;
import ru.skillbox.diplom.repositories.UserRepositori;
import ru.skillbox.diplom.services.CaptchaService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiAuthController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private CaptchaRepositori captchaRepositori;

    @Autowired
    private UserRepositori userRepositori;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private HttpServletRequest reg;

    private Map<String, Integer> auth = new HashMap<>();

    @PostMapping("/auth/login")
    public UserResponseAuth login(@RequestBody Login login) {
        logger.info("/auth/login");

        Optional<User> user = userRepositori.findByEmail(login.getE_mail());
        if (user.isPresent() && new BCryptPasswordEncoder().matches(login.getPassword(), user.get().getPassword())) {
            auth.put(reg.getSession().getId(), user.get().getId());
            return new UserResponseAuth(true, UserMapper.getUser(user.get()));
        }
        return new UserResponseAuth(false);
    }

    @GetMapping("/auth/logout")
    public ResponseAll logout() {
        auth.clear();
        return new ResponseAll(true);
    }

    @GetMapping("/auth/check")
    public UserResponseAuth check() {
        if (Objects.isNull(auth.get(reg.getSession().getId()))) {
            logger.info("/auth/check : false");
            return new UserResponseAuth(false);
        }
        logger.info("/auth/check : true");
        return new UserResponseAuth(
                true,
                UserMapper.getUser(userRepositori.getOne(auth.get(reg.getSession().getId())))
        );
    }

    @GetMapping(value = "/auth/captcha")
    public CaptchaResponse genCaptcha() {

        CaptchaResponse captchaResponse = new CaptchaResponse();
        CaptchaCode captchaCode = captchaService.genAndSaveCaptcha();
        captchaResponse.setImage("data:image/png;base64," + captchaCode.getSecretCode());
        captchaResponse.setSecret(Base64.getEncoder().encodeToString(captchaCode.getCode().getBytes()));

        return captchaResponse;
    }

    @PostMapping(value = "/auth/register")
    public ResponseAll register(@RequestBody Register register) {
        User addUser = new User();
        logger.info("/auth/register" + " -> email:" + register.getE_mail());

        if (captchaRepositori.findByCode(register.getCaptcha()).isEmpty()) {
            return new ResponseAll(false, "captcha", "Код с картинки введён неверно");
        } else if (userRepositori.findByEmail(register.getE_mail()).isPresent()) {
            return new ResponseAll(false, "email", "Этот e-mail уже зарегистрирован");
        } else {
            addUser.setModerator(false);
            addUser.setRegTime(System.currentTimeMillis());
            addUser.setName("нЕкто");
            addUser.setEmail(register.getE_mail());
            addUser.setPhoto("default.jpg");
            addUser.setPassword(new BCryptPasswordEncoder().encode(register.getPassword()));
            userRepositori.save(addUser);
            //TODO проверку тут сделать что юзер записался

            return new ResponseAll(true);
        }

    }
}