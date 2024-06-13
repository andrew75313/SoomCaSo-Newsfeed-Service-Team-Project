package com.sparta.newsfeedteamproject.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "RequestLogAop")
@Aspect
@Component
public class RequestLogAop {
    @Pointcut("execution(public * com.sparta.newsfeedteamproject.controller.*.*(..))")
    private void controller(){}

    @Before("controller()")
    public void showRequestLog(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String url = request.getRequestURI();
        String httpMethod = request.getMethod();

        log.info("[Request URL] : " + url + " [HTTP Method] : " + httpMethod);
    }

}

