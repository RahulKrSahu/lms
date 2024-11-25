package com.example.lms.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String genre;

    @Column(nullable = false)
    private Double price;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}
