package ru.skillbox.diplom.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.Mapper.Constant;
import ru.skillbox.diplom.Mapper.UserMapper;
import ru.skillbox.diplom.api.requests.EmailRequest;
import ru.skillbox.diplom.api.requests.Login;
import ru.skillbox.diplom.api.requests.PasswordRequest;
import ru.skillbox.diplom.api.requests.Register;
import ru.skillbox.diplom.api.responses.CaptchaResponse;
import ru.skillbox.diplom.api.responses.UserResponse;
import ru.skillbox.diplom.api.responses.UserResponseAuth;
import ru.skillbox.diplom.entities.CaptchaCode;
import ru.skillbox.diplom.entities.EModerationStatus;
import ru.skillbox.diplom.entities.User;
import ru.skillbox.diplom.repositories.CaptchaRepositori;
import ru.skillbox.diplom.repositories.PostRepositori;
import ru.skillbox.diplom.repositories.UserRepositori;
import ru.skillbox.diplom.services.CaptchaService;
import ru.skillbox.diplom.services.EMailService;
import ru.skillbox.diplom.services.PasswordService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
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
    private EMailService eMailService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private PostRepositori postRepositori;

    @Autowired
    private HttpServletRequest reg;

    @PostMapping("/auth/login")
    public ResponseEntity<UserResponseAuth> login(@RequestBody(required = false) Login login) {
        logger.info("/auth/login");
        Optional<User> user = userRepositori.findByEmail(login.getE_mail());
        if (user.isPresent() && new BCryptPasswordEncoder().matches(login.getPassword(), user.get().getPassword())) {
            Constant.auth.put(reg.getSession().getId(), user.get().getId());
            UserResponse userResponse = UserMapper.getFullUser(userRepositori.getOne(Constant.auth.get(reg.getSession().getId())));
            if (userResponse.isModeration())
                userResponse.setModerationCount(postRepositori.countByeModerationStatusAndIsActive(EModerationStatus.NEW, true));
            return new ResponseEntity<>(new UserResponseAuth(true, userResponse), HttpStatus.OK);
        }
        return new ResponseEntity<>(new UserResponseAuth(false), HttpStatus.OK);
    }

    //TODO косячно пока, переделать
    @GetMapping("/auth/logout")
    public ResponseEntity<Map> logout() {
        logger.info("/auth/logout");
        Constant.auth.clear();
        return new ResponseEntity<>(Constant.responseTrue(), HttpStatus.OK);
    }

    @GetMapping("/auth/check")
    public UserResponseAuth check() {
        if (Objects.isNull(Constant.auth.get(reg.getSession().getId()))) {
            logger.info("/auth/check : false");
            return new UserResponseAuth(false);
        }
        logger.info("/auth/check : true");
        UserResponse userResponse = UserMapper.getFullUser(userRepositori.getOne(Constant.auth.get(reg.getSession().getId())));
        if (userResponse.isModeration())
            userResponse.setModerationCount(postRepositori.countByeModerationStatusAndIsActive(EModerationStatus.NEW, true));
        return new UserResponseAuth(true, userResponse);
    }

    @GetMapping(value = "/auth/captcha")
    public ResponseEntity<CaptchaResponse> genCaptcha() {

        CaptchaResponse captchaResponse = new CaptchaResponse();
        CaptchaCode captchaCode = captchaService.genAndSaveCaptcha();
        captchaResponse.setImage("data:image/png;base64," + captchaCode.getSecretCode());
        captchaResponse.setSecret(Base64.getEncoder().encodeToString(captchaCode.getCode().getBytes()));

        return new ResponseEntity<>(captchaResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/auth/register")
    public ResponseEntity<Map> register(@RequestBody(required = false) Register register) {
        User addUser = new User();
        logger.info("/auth/register" + " -> email:" + register.getE_mail());

        if (captchaRepositori.findByCode(register.getCaptcha()).isEmpty()) {
            return new ResponseEntity<>(Constant.responseError("captcha", "Код с картинки введён неверно"), HttpStatus.OK);
        } else if (userRepositori.findByEmail(register.getE_mail()).isPresent()) {
            return new ResponseEntity<>(Constant.responseError("email", "Этот e-mail уже зарегистрирован"), HttpStatus.OK);
        } else {
            addUser.setModerator(false);
            addUser.setRegTime(System.currentTimeMillis());

            if (register.getName().isEmpty()) {
                addUser.setName("нЕкто");;
            } else {
                addUser.setName(register.getName());;
            }
            addUser.setEmail(register.getE_mail().toLowerCase());
            addUser.setPhoto("default.jpg");
            addUser.setPassword(new BCryptPasswordEncoder().encode(register.getPassword()));
            userRepositori.save(addUser);
            //TODO проверку тут сделать что юзер записался

            return new ResponseEntity<>(Constant.responseTrue(), HttpStatus.OK);
        }

    }

    @PostMapping("/auth/restore")
    public ResponseEntity<Map> restorePassword(@RequestBody(required = false) EmailRequest email) throws UnsupportedEncodingException, MessagingException {
        logger.info("/auth/restore");
        Optional<User> currentUser = userRepositori.findByEmail(email.getEmail().toLowerCase());
        if (currentUser.isEmpty()) return new ResponseEntity<>(Constant.responseFalse(), HttpStatus.OK);
        String code = Constant.codeGenerator(10);
        User userWithNewCode = currentUser.get();
        userWithNewCode.setCode(code);
        userRepositori.save(userWithNewCode);
        if (passwordService.passwordRecovery(email.getEmail(), code))
            return new ResponseEntity<>(Constant.responseTrue(), HttpStatus.OK);
        return new ResponseEntity<>(Constant.responseFalse(), HttpStatus.OK);
    }

    @PostMapping("auth/password")
    public ResponseEntity<Map> newPassword(@RequestBody(required = false) PasswordRequest passwordRequest) {
        logger.info("auth/password");
        if (captchaRepositori.findByCode(passwordRequest.getCaptcha()).isEmpty()) {
            return new ResponseEntity<>(Constant.responseError("captcha", "Код с картинки введён неверно"), HttpStatus.OK);
        } else if (userRepositori.findByCode(passwordRequest.getCode()).isEmpty()) {
            return new ResponseEntity<>(Constant.responseError("code", "Ссылка для восстановления пароля устарела.\n" +
                    "<a href=”/auth/restore”>Запросить ссылку снова</a>"), HttpStatus.OK);
        } else {
            Optional<User> newPasswordUser = userRepositori.findByCode(passwordRequest.getCode());
            User user = newPasswordUser.get();
            user.setPassword(new BCryptPasswordEncoder().encode(passwordRequest.getPassword()));
            userRepositori.save(user);
            return new ResponseEntity<>(Constant.responseTrue(), HttpStatus.OK);
        }
    }
}