package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.UserDto;
import com.example.librarymanagement.dto.authentication.JwtResponse;
import com.example.librarymanagement.dto.authentication.Login;
import com.example.librarymanagement.dto.authentication.SignUp;
import com.example.librarymanagement.entity.User;
import io.viascom.nanoid.NanoId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;

    public UserDto signUp(SignUp register) {
        if (!userService.validateSignUpUser(register.getUsername())) {
            User user = new User();
            user.setOid(NanoId.generate(10));
            user.setUsername(register.getUsername());
            user.setPassword(passwordEncoder.encode(register.getPassword()));
            user.setRole(User.Role.USER);
            user.setName(register.getName());
            User userResult = userService.createUser(user);
            return modelMapper.map(userResult, UserDto.class);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
    }

    public JwtResponse Login(Login account) {
        userService.getByUsername(account.getUsername()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found"));
        return  null;
    }
}
