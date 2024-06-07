package com.sparta.newsfeedteamproject.security;

import com.sparta.newsfeedteamproject.exception.FilterExceptionHandler;
import com.sparta.newsfeedteamproject.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j(topic = "JWT 검증 및 인가")
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthorizationFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String accessTokenValue = jwtProvider.getJwtFromHeader(req, JwtProvider.ACCESS_TOKEN_HEADER);
        String refreshTokenValue = jwtProvider.getJwtFromHeader(req, JwtProvider.REFRESH_TOKEN_HEADER);

        if (StringUtils.hasText(accessTokenValue)) {
            try {
                // JWT 토큰 substring
                accessTokenValue = jwtProvider.substringToken(accessTokenValue);
                refreshTokenValue = jwtProvider.substringToken(refreshTokenValue);

                log.info(accessTokenValue);
                log.info(refreshTokenValue);

                Claims info = jwtProvider.getUserInfoFromToken(accessTokenValue);

                //둘 다 유효하지 않을 때
                if (!jwtProvider.isTokenValidate(accessTokenValue) && !jwtProvider.isTokenValidate(refreshTokenValue)) {
                    throw new IllegalArgumentException("유효하지 않은 토큰입니다. 다시 로그인해주세요.");
                }

                //로그아웃 요청일 땐 Header에 토큰 추가 X
                if (!"users/logout".equals(req.getRequestURI())) {
                    if ((!jwtProvider.isTokenValidate(accessTokenValue) && jwtProvider.isTokenValidate(refreshTokenValue)) //refresh만 정상일 때
                            || (jwtProvider.isTokenValidate(accessTokenValue) && !jwtProvider.isTokenValidate(refreshTokenValue))) { //access만 정상일 때
                        //토큰 재생성
                        jwtProvider.reCreateTokens(info.getSubject(), res);
                        log.info("토큰 재생성 완료");
                    } else { //두 토큰 다 정상일 때
                        res.addHeader(JwtProvider.ACCESS_TOKEN_HEADER, accessTokenValue);
                        res.addHeader(JwtProvider.REFRESH_TOKEN_HEADER, refreshTokenValue);
                    }
                }

                UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetailsService.loadUserByUsername(info.getSubject());

                if (!(Objects.equals(refreshTokenValue, jwtProvider.substringToken((userDetailsImpl.getUser().getRefreshToken()))))) {
                    throw new IllegalArgumentException("유효하지 않은 토큰입니다. 다시 로그인해주세요.2");
                }

                setAuthentication(info.getSubject());

            } catch (Exception e) {
                log.error(e.getMessage());
                FilterExceptionHandler.handleExceptionInFilter(res, e);
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
