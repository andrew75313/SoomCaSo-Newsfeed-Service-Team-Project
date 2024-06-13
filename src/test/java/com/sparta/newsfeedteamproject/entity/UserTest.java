package com.sparta.newsfeedteamproject.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    @DisplayName("User update test")
    public void test(){
        //given
        User user = new User();
        String name = "name";
        String userInfo = "userInfo";
        String newPassword = "newPassword";

        //when
        user.update(name,userInfo,newPassword);

        //then
        assertEquals(name,user.getName());
        assertEquals(userInfo,user.getUserInfo());
        assertEquals(newPassword,user.getPassword());

    }

    @Test
    @DisplayName("User updateRefreshToken test")
    public void test2(){
        //given
        User user = new User();
        String refreshToken = "refreshToken";

        //when
        user.updateRefreshToken(refreshToken);

        //then
        assertEquals(refreshToken,user.getRefreshToken());

    }
}