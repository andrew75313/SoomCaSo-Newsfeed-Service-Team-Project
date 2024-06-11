package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.MessageResDto;
import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.exception.ExceptionMessage;
import com.sparta.newsfeedteamproject.util.RedisUtil;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private final UserService userService;
    @Value("${spring.mail.username}")
    private String configEmail;

    public int createNumber() {
        return (int) (Math.random() * (90000)) + (100000);
    }

    public String setContext(String authCode) {
        Context context = new Context();
        TemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        context.setVariable("code", authCode);

        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("mail", context);
    }

    public MimeMessage createMail(String email) throws MessagingException {
        String authNumber = Integer.toString(createNumber());
        log.info("authNumber : " + authNumber);

        MimeMessage message = javaMailSender.createMimeMessage();
        message.setRecipients(Message.RecipientType.TO, email);
        message.setSubject("이메일 인증");
        message.setFrom(configEmail);
        message.setText(setContext(authNumber), "utf-8", "html");

        redisUtil.setDataExpire(email, authNumber, 60 * 3);

        return message;
    }

    public void sendMail(String email) throws MessagingException {
        User user = userService.findByEmail(email);
        if (!user.getStatus().equals(Status.UNAUTHORIZED)) {
            throw new IllegalArgumentException(ExceptionMessage.AUTHENTICATED_USER.getExceptionMessage());
        }
        if (!user.getEmail().equals(email)) {
            throw new IllegalArgumentException(ExceptionMessage.INCORRECT_USER.getExceptionMessage());
        }
        if (redisUtil.existData(email)) {
            redisUtil.deleteData(email);
        }

        MimeMessage message = createMail(email);
        javaMailSender.send(message);
    }

    public boolean verifyEmailCode(String email, String code) {
        String codeFindByEmail = redisUtil.getData(email);
        if (codeFindByEmail == null) {
            throw new IllegalArgumentException(ExceptionMessage.NON_EXISTENT_ELEMENT.getExceptionMessage());
        }
        return codeFindByEmail.equals(code);
    }

    @Transactional
    public MessageResDto signupEmailVerify(String email, String code) {
        if (!verifyEmailCode(email, code)) {
            return new MessageResDto(HttpStatus.BAD_REQUEST.value(), "인증번호 검증에 실패했습니다.", null);
        }
        User user = userService.findByEmail(email);
        user.setStatus(Status.ACTIVATE);
        redisUtil.deleteData(email);
        return new MessageResDto(HttpStatus.OK.value(), "인증번호 검증에 성공했습니다.", null);
    }
}