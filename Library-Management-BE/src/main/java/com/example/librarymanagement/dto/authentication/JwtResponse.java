package com.example.librarymanagement.dto.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class JwtResponse {
    private String access_token;
    private String refresh_token;

    public JwtResponse(String token, String refresh_token) {
        this.access_token = token;
        this.refresh_token = refresh_token;
    }

    public JwtResponse(String token) {
        this.access_token = token;
    }
}