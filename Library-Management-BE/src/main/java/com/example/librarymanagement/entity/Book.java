package com.example.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 200)
    private String path;

    @ManyToOne
    @JoinColumn(name = "categories_id", nullable = false)
    private Category category;
}
