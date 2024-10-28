package com.lotteon.entity.product;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name="review")
public class Review {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;
    private String writer;
    private LocalDateTime rdate;
    private String title;
    private String content;
    private String rating;  //상품평점

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewFile> pReviewFiles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
