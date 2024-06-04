package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.dto.user.SignupReqDto;
import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signup(SignupReqDto reqDto) {

        String login_id = reqDto.getLogin_id();
        String password = bCryptPasswordEncoder.encode(reqDto.getPassword());
        String name = reqDto.getName();
        String email = reqDto.getEmail();
        String user_info = reqDto.getUser_info();

        Optional<User> checkLogin_id = userRepository.findByLogin_id(login_id);
        if (checkLogin_id.isPresent()) {
            throw new IllegalArgumentException("중복된 로그인 아이디 입니다.");
        }

        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }

        Status status = Status.ACTIVATE;

        User user = new User(login_id,password,name,email,user_info,status);
    }
}
