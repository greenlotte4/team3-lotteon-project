package com.lotteon.entity.product;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Getter
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="review_id")
    private List<ReviewFile> pReviewFiles;

}
