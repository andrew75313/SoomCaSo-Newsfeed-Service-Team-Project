package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.user.SignupReqDto;
import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signup(SignupReqDto reqDto) {

        String username = reqDto.getUsername();
        String password = bCryptPasswordEncoder.encode(reqDto.getPassword());
        String name = reqDto.getName();
        String email = reqDto.getEmail();
        String userInfo = reqDto.getUserInfo();

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자 이름 입니다.");
        }

        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }

        Status status = Status.ACTIVATE;
        LocalDateTime statusModTime = LocalDateTime.now();

        User user = new User(username,password,name,email,userInfo,status,statusModTime);
        userRepository.save(user);
    }
}
