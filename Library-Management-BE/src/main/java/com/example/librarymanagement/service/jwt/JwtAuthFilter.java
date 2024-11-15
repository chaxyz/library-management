package com.example.librarymanagement.service.jwt;

import com.example.librarymanagement.entity.User;
import com.example.librarymanagement.exception.ErrorResponse;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.utils.JwtTokenUtil;
import com.example.librarymanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String oid = null;
        String jwtToken = null;
        // token ถูก
        if (requestTokenHeader != null) {
            if (requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    oid = jwtTokenUtil.getOidFromToken(jwtToken);
                } catch (SecurityException | MalformedJwtException e) {
                    request.setAttribute("Error-Message", "Invalid JWT token");
                } catch (ExpiredJwtException e) {
                    request.setAttribute("Error-Message", "Expired JWT token");
                } catch (UnsupportedJwtException e) {
                    request.setAttribute("Error-Message", "Unsupported JWT token");
                } catch (IllegalArgumentException e) {
                    request.setAttribute("Error-Message", "Missing JWT token");
                } catch (Exception e) {
                    request.setAttribute("Error-Message", "Authentication failure");
                }
            } else {
                request.setAttribute("Error-Message", "Token not starting with Bearer");
            }
        }


        if (oid != null) {
            User user = userService.loadUserByOid(oid);
            if (user == null) {
                request.setAttribute("Error-Message", "User not found");
            } else {
                String username = user.getUsername();
                try {
                    if (request.getServletPath().equals("/token")) {
                        chain.doFilter(request, response);
                        return;
                    }
                    UserDetails userDetails = userService.loadUserByUsername(username);
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                        // token valid and userDetails not null (owner)
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                } catch (Exception ex) {
                    writeErrorResponse(response, HttpStatus.UNAUTHORIZED, ex.getMessage());
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    private void writeErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponse errorResponse = new ErrorResponse(
                Timestamp.from(Instant.now()),
                status.value(),
                status.getReasonPhrase(),
                message,
                null,
                null
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

}

