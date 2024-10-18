package com.lotteon.entity;

import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "prodCategory")
public class ProdCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer level;

    private String subcategory;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ProdCategory parent;

    // Getters and Setters
}

