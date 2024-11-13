package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.UserDto;
import com.example.librarymanagement.dto.authentication.JwtResponse;
import com.example.librarymanagement.dto.authentication.Login;
import com.example.librarymanagement.dto.authentication.SignUp;
import com.example.librarymanagement.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    AuthenticationManager authenticationManager;
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody @Valid SignUp signup){
        System.out.println("test5646");
        return  ResponseEntity.ok(authenticationService.signUp(signup));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody Login login){;
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        if (!authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        System.out.println("sucess");
        return  null;
    }

}
