package com.sparta.newsfeedteamproject.controller;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "이메일 인증 컨트롤러")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class MailController {

    private final MailService mailService;

    @GetMapping("/signup/{emailAddr:.+}/authcode")
    public ResponseEntity<BaseResDto> sendMail(@PathVariable @NotBlank @Email String emailAddr) throws MessagingException {
        mailService.sendMail(emailAddr);
        BaseResDto responsDto = new BaseResDto(HttpStatus.OK.value(), "인증번호가 메일로 발송되었습니다.", null);
        return new  ResponseEntity<>(responsDto, HttpStatus.OK);
    }

    @PostMapping("/signup/{emailAddr}/authcode")
    public ResponseEntity<BaseResDto> checkMail(@PathVariable @NotBlank @Email String emailAddr, @RequestParam @NotBlank @Positive String code) {
        BaseResDto responsDto = mailService.signupEmailVerify(emailAddr, code);
        return new ResponseEntity<>(responsDto, HttpStatus.OK);
    }
}