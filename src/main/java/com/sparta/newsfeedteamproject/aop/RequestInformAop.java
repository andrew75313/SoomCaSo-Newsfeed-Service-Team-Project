package com.sparta.newsfeedteamproject.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "API Request 정보")
@Component
@Aspect
public class RequestInformAop {

    @Pointcut("execution(* com.sparta.newsfeedteamproject.controller..*(..))")
    private void controller() {};

    @Before("controller()")
    public void loggingRequestInformForAllController() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        log.info("HTTP Method : {}", request.getMethod());
        log.info("Request URI : {}", request.getRequestURI());
    }
}
