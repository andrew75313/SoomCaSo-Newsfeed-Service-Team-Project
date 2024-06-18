package com.sparta.newsfeedteamproject.service;

import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("test") //test 프로파일에서만 활성화 되는 클래스
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init(){
        //초기 데이터 삽입
        User user = new User("ggumi12345", "Ggumi1234567!", "김꾸미", "ggumi@gmail.com", "안녕하세요", Status.UNAUTHORIZED);
        userRepository.save(user);
    }
}
