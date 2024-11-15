package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.UserDto;
import com.example.librarymanagement.dto.authentication.JwtResponse;
import com.example.librarymanagement.dto.authentication.Login;
import com.example.librarymanagement.dto.authentication.SignUp;
import com.example.librarymanagement.entity.User;
import com.example.librarymanagement.utils.JwtTokenUtil;
import io.viascom.nanoid.NanoId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
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
        User user = userService.getByUsername(account.getUsername()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found"));
        String token = jwtTokenUtil.generateToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user);
        return  new JwtResponse(token,refreshToken);
    }

    public JwtResponse refresh(String token) {
        try {
            String onlyToken = null;
            if (token.startsWith("Bearer ")) {
                onlyToken = token.substring(7);
            }
            if (!jwtTokenUtil.validateRefreshToken(onlyToken) || onlyToken == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh-token");
            }
            String oid = jwtTokenUtil.getOidFromToken(onlyToken);
            User user = userService.loadUserByOid(oid);
            String newToken = jwtTokenUtil.generateToken(user);
            return new JwtResponse(newToken);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh-token");
        }
    }
}
