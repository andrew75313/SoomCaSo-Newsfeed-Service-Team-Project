package com.sparta.newsfeedteamproject.jwt;

import com.sparta.newsfeedteamproject.entity.Status;
import com.sparta.newsfeedteamproject.entity.User;
import com.sparta.newsfeedteamproject.service.UserService;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.sparta.newsfeedteamproject.config.JwtConfig.*;

@Slf4j(topic = "JwtProvider")
@Component
public class JwtProvider {

    private final UserService userService;

    public JwtProvider(UserService userService) {
        this.userService = userService;
    }

    public String createAccessToken(String username, Status status) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .setExpiration(new Date(date.getTime() + accessTokenTime))
                        .claim(AUTHORIZATION_KEY, status)
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    @Transactional
    public String createRefreshToken(String username, Status status) {
        Date date = new Date();

        String refreshToken = BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .setExpiration(new Date(date.getTime() + refreshTokenTime))
                        .claim(AUTHORIZATION_KEY, status)
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
        User user = userService.findByUsername(username);
        user.updateRefreshToken(refreshToken);
        return refreshToken;
    }

    public String getJwtFromHeader(HttpServletRequest request, String tokenHeaderValue) {
        return request.getHeader(tokenHeaderValue);
    }

    public String substringToken(String token) {
        if (!token.startsWith(BEARER_PREFIX)) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        return token.substring(7);
    }


    public boolean isTokenValidate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    @Transactional
    public void reCreateTokens(String username, HttpServletResponse response) {

        User user = userService.findByUsername(username);
        String accessToken = createAccessToken(user.getUsername(), user.getStatus());
        String refreshToken = createRefreshToken(user.getUsername(), user.getStatus());

        response.addHeader(ACCESS_TOKEN_HEADER, accessToken);
        response.addHeader(REFRESH_TOKEN_HEADER, refreshToken);
    }
}