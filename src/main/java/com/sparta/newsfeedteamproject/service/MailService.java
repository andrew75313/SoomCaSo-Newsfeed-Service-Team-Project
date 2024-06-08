package com.sparta.newsfeedteamproject.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender javaMailSender;
    private static final String SENDEREMAIL = "team.pigeon9999@gmail.com";
    private static int authNumber;

    public static void createNumber() {
        authNumber = (int)(Math.random() * (90000)) + (100000);
    }

    public MimeMessage createMail(String email) {
        createNumber();
        log.info(String.valueOf(authNumber));
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(SENDEREMAIL);
            log.info("setFrom");
            message.setRecipients(Message.RecipientType.TO, email);
            log.info("setRecipients");
            message.setSubject("이메일 인증");
            log.info("info");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h3>" + authNumber + "</h3>";
            body += "<h3>" + "감사합니다" + "</h3>";
            log.info("body");
            message.setText(body, "UTF-8", "html");
            log.info("setText");

        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendMail(String email) {
        MimeMessage message = createMail(email);
        javaMailSender.send(message);
        log.info("mail send");

        return authNumber;
    }
}
