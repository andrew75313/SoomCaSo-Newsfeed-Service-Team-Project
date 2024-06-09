package com.sparta.newsfeedteamproject.controller;

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
    public ResponseEntity<String> sendMail(@PathVariable String email_addr) throws MessagingException {
        mailService.sendMail(email_addr);
        return ResponseEntity.ok("메일을 확인하세요");
    }

    @PostMapping("/signup/{email_addr}/authcode")
    public ResponseEntity<String> checkMail(@PathVariable String email_addr, @RequestParam String code) {
        mailService.signupEmailVerify(email_addr, code);
        return new ResponseEntity<>("인증번호 검증이 완료되었습니다.", HttpStatus.OK);
    }
}
