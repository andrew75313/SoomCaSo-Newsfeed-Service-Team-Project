package com.sparta.newsfeedteamproject.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j(topic = "AuthFilterException")
public class FilterExceptionHandler {
    public static <T extends Exception> void handleExceptionInFilter(HttpServletResponse servletResponse, T exception) {
        log.error(exception.getMessage());

        //서블릿 응답 UTF-8 인코딩
        servletResponse.setContentType("text/plain; charset=UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");

        servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try {
            servletResponse.getWriter().write("error :" + exception.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void handleJwtExceptionInFilter(HttpServletResponse servletResponse, ExceptionMessage errorMessage) {
        log.error(errorMessage.getExceptionMessage());

        //서블릿 응답 UTF-8 인코딩
        servletResponse.setContentType("text/plain; charset=UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");

        servletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        try {
            servletResponse.getWriter().write("error :" + errorMessage);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
