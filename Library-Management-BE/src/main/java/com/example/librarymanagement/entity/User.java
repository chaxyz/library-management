package com.example.librarymanagement.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @Column(length = 10, nullable = false)
    private String oid;

    @Column(length = 100, nullable = false)
    private String username;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public enum Role {
        ADMIN, USER
    }
}

