package com.sparta.newsfeedteamproject.controller;

import com.sparta.newsfeedteamproject.service.MailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j(topic = "이메일 인증 컨트롤러")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private int number;

    @PostMapping("/signup/authenticationEmail")
    public HashMap<String, Object> sendMail(@RequestParam String email) {
        log.info(email);
        HashMap<String, Object> map = new HashMap<>();

        try {
            number = mailService.sendMail(email);
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

    @GetMapping("/signup/emailCheck")
    public ResponseEntity<String> checkMail(@RequestParam String authNumber) {
        if (!authNumber.equals(String.valueOf(number))) {
            return new ResponseEntity<>("인증번호 검증을 실패했습니다. 인증번호를 확인해주세요.", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("인증번호 검증이 완료되었습니다.", HttpStatus.OK);
    }
}
