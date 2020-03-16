package ru.skillbox.diplom.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class EMailService {
    @Value("${email.host}")
    private String mailServerHostName;
    @Value("${email.port}")
    private int mailServerPort;
    @Value("${email.username}")
    private String mailServerUsername;
    @Value("${email.password}")
    private String mailServerPassword;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Properties emailProperties = new Properties();

    {
        emailProperties.put("mail.transport.protocol", "smtp");
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        emailProperties.put("mail.smtp.starttls.required", "true");
    }

    public boolean sendEmail(String email, String subjectEmail, String messageHtml) throws MessagingException, UnsupportedEncodingException {
        String recipientEmail = email;//TODO переделать
        String subject = subjectEmail;//TODO переделать
        String messageTextBody = "";
        String messageHtmlBody = messageHtml;//TODO переделать
        Session mailSession = Session.getInstance(emailProperties);
        Transport transport = null;

        boolean result = true;

        try {
            //region Создание multipart контента
            Multipart content = new MimeMultipart("alternative");
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(messageTextBody, "text/plain");
            content.addBodyPart(textPart);
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(messageHtmlBody, "text/html;charset=\"UTF-8\"");
            //endregion

            //region Инициализация MimeMessage
            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(subject, StandardCharsets.UTF_8.name());
            content.addBodyPart(htmlPart);
            message.setContent(content);
            message.setFrom(new InternetAddress(mailServerUsername, "Diplom"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            //endregion
            transport = mailSession.getTransport();
            transport.connect(mailServerHostName, mailServerPort, mailServerUsername, mailServerPassword);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        } catch (MessagingException e) {
            logger.error("Ошибка -> " + email);
            result = false;
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    logger.error("Ошибка при закрытии transport " + e.toString());
                    result = false;
                }
            }
        }
        return result;

    }
}
