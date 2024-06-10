package com.sparta.newsfeedteamproject.controller;

import com.sparta.newsfeedteamproject.dto.BaseResDto;
import com.sparta.newsfeedteamproject.service.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "이메일 인증 컨트롤러")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping("/signup/{email_addr}/authcode")
    public ResponseEntity<BaseResDto> sendMail(@PathVariable String email_addr) throws MessagingException {
        mailService.sendMail(email_addr);
        BaseResDto responsDto = new BaseResDto(HttpStatus.OK.value(), "인증번호가 메일로 발송되었습니다.", null);
        return new  ResponseEntity<>(responsDto, HttpStatus.OK);
    }

    @PostMapping("/signup/{email_addr}/authcode")
    public ResponseEntity<BaseResDto> checkMail(@PathVariable String email_addr, @RequestParam String code) {
        BaseResDto responsDto = mailService.signupEmailVerify(email_addr, code);
        return new ResponseEntity<>(responsDto, HttpStatus.OK);
    }
}