package com.example.librarymanagement.repository;


import com.example.librarymanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    boolean existsByOidAndUsernameAndRole(String oid, String username, User.Role role);

    boolean existsByOid(String oid);

    Optional<User> findByOid(String oid);
}

