package com.example.librarymanagement.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        String errorMessage = (String) request.getAttribute("Error-Message");
        System.out.println("CustomAuthenticationEntryPoint");
        ErrorResponse errorResponse = new ErrorResponse(
                Timestamp.from(Instant.now()),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                errorMessage,
                request.getRequestURI(),
                null
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}