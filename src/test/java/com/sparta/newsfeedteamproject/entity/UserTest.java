package com.sparta.newsfeedteamproject.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    String username = "testUser";
    String password = "testPass";
    String name = "testName";
    String email = "test@test@email.com";
    String userInfo = "hello";
    Status status = Status.ACTIVATE;
    LocalDateTime statusModTime = LocalDateTime.now();
    String NULL_USERNAME = "Username은 null일 수 없습니다";
    String NULL_PASSWORD = "Password는 null일 수 없습니다.";
    String NULL_NAME = "Name은 null일 수 없습니다.";
    String NULL_EMAIL = "Email은 null일 수 없습니다.";
    String NULL_STATUS = "Status는 null일 수 없습니다.";
    String NULL_STATUSMODTIME = "StatusModTime은 null일 수 없습니다.";

    void validateUser(User user) {
        if (user.getUsername() == null) throw new IllegalArgumentException(NULL_USERNAME) ;
        if (user.getPassword() == null) throw new IllegalArgumentException(NULL_PASSWORD);
        if (user.getName() == null) throw new IllegalArgumentException(NULL_NAME);
        if (user.getEmail() == null) throw new IllegalArgumentException(NULL_EMAIL);
        if (user.getStatus() == null) throw new IllegalArgumentException(NULL_STATUS);
        if (user.getStatusModTime() == null) throw new IllegalArgumentException(NULL_STATUSMODTIME);
    }

    void validateRefreshToken(User user) {
        if (user.getRefreshToken() == null) throw new IllegalArgumentException("RefreshToken이 Null입니다.");
    }

    @Test
    @DisplayName("User Entity 생성 성공 테스트")
    void testCreateUserSuccess() {
        // given, when 객체 생성
        User user = new User(username, password, name, email, userInfo, status, statusModTime);

        // then
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertEquals(userInfo, user.getUserInfo());
        assertEquals(status, user.getStatus());
        assertEquals(statusModTime, user.getStatusModTime());
    }

    @Test
    @DisplayName("User Entity 생성 실패 테스트")
    void testCreateUserFail() {
        // given
        User user = new User(null, password, name, email, userInfo, status, statusModTime);

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validateUser(user);
        });

        // then
        assertTrue(exception.getMessage().contains(NULL_USERNAME));
    }

    @Test
    @DisplayName("deleteRefreshToken 메서드 성공 테스트")
    void testDeleteRefreshToken() {
        // given
        User user = new User(username, password, name, email, userInfo, status, statusModTime);
        user.setRefreshToken("thisIsToken");

        // when
        user.deleteRefreshToken();

        // then
        assertEquals("", user.getRefreshToken());
    }

    // deleteRefreshToken 실패 케이스는 없음

    @Test
    @DisplayName("update 메서드 성공 테스트")
    void testUpdateSuccess() {
        // given
        User user = new User(username, password, name, email, userInfo, status, statusModTime);
        String newName = "newName";
        String newUserInfo = "new Info";
        String newPassword = "newPass";
        LocalDateTime newModifiedAt = LocalDateTime.now().plusDays(1);

        // when
        user.update(newName, newUserInfo, newPassword, newModifiedAt);

        // then
        assertEquals(newName, user.getName());
        assertEquals(newUserInfo, user.getUserInfo());
        assertEquals(newPassword, user.getPassword());
        assertEquals(newModifiedAt, user.getModifiedAt());
    }

    @Test
    @DisplayName("update 메서드 실패 테스트")
    void testUpdateFail() {
        // given
        User user = new User(username, password, name, email, userInfo, status, statusModTime);
        String newUserInfo = "new Info";
        String newPassword = "newPass";
        LocalDateTime newModifiedAt = LocalDateTime.now().plusDays(1);

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.update(null, newUserInfo, newPassword, newModifiedAt);
            validateUser(user);
        });

        // then
        assertTrue(exception.getMessage().contains(NULL_NAME));
    }

    @Test
    @DisplayName("updateRefreshToken 메서드 성공 테스트")
    void testUpdateRefreshTokenSuccess() {
        // given
        User user = new User(username, password, name, email, userInfo, status, statusModTime);
        String newRefreshToken = "newToken";

        // when
        user.updateRefreshToken(newRefreshToken);

        // then
        assertEquals(newRefreshToken, user.getRefreshToken());
    }

    @Test
    @DisplayName("updateRefreshToken 메서드 실패 테스트")
    void testUpdateRefreshTokenFail() {
        // given
        User user = new User(username, password, name, email, userInfo, status, statusModTime);

        // when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            user.updateRefreshToken(null);
            validateRefreshToken(user);
        });


        // then
        assertTrue(exception.getMessage().contains("RefreshToken이 Null입니다."));
    }
}