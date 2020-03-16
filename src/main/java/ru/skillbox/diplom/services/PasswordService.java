package ru.skillbox.diplom.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Service
public class PasswordService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private EMailService eMailService;

    public Boolean passwordRecovery(String email, String code) throws UnsupportedEncodingException, MessagingException {
        logger.info("passwordRecovery ->" + email);
        String subject = "Восстановление пароля для сайта " + httpServletRequest.getServerName();
        String serverName = "";
        if(httpServletRequest.getServerName().equals("localhost")) {
            serverName = "http://" + httpServletRequest.getServerName() + ":8080" + "/login/change-password/" + code;
        } else {
            serverName = "http://" + httpServletRequest.getServerName() + "/login/change-password/" + code;
        }
        String message = "Для восстановления пароля на сайте " + httpServletRequest.getServerName() + "<br>" +
                "нужно перейди по <a href=\""+serverName+"\">ссылке</a> ";

        return eMailService.sendEmail(email, subject, message);
    }
}
