package com.example.librarymanagement.service;

import com.example.librarymanagement.entity.AuthUser;
import com.example.librarymanagement.entity.User;
import com.example.librarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public boolean validateSignUpUser(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> roles = new ArrayList<>();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        roles.add(new SimpleGrantedAuthority(user.getRole().toString().toUpperCase()));
        return new AuthUser(username, user.getPassword() , roles);
    }


    public User loadUserByOid(String oid) {
        return  userRepository.findByOid(oid).orElseThrow(() -> new UsernameNotFoundException(oid));
    }


}
