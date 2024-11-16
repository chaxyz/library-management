package com.example.librarymanagement.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "books")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 200)
    private String path;

    @ManyToOne
    @JoinColumn(name = "categories_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rent.Status status;

}
