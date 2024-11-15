package com.example.librarymanagement.dto.authentication;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUp {
    @Size(min = 8, max = 100)
    private String username;
    @Size(max = 50)
    private String name;
    @Size(min = 8, max = 255)
    private String password;
}

