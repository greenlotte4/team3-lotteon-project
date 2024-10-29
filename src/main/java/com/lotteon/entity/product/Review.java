package com.lotteon.entity.product;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.dto.product.ReviewDTO;
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
    @JsonBackReference // 순환 참조 방지를 위해 사용
    private List<ReviewFile> pReviewFiles = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference // 순환 참조 방지를 위해 사용
    private Product product;

    public ReviewDTO ToDTO(Review review) {
        ProductDTO productDTO = null;
        if (review.getProduct() != null) {
            productDTO = ProductDTO.builder()
                    .productId(review.getProduct().getProductId())
                    .productName(review.getProduct().getProductName())
                    // 필요한 다른 필드도 설정
                    .build();
        }

        return ReviewDTO.builder()
                .reviewId(review.getReviewId())
                .writer(review.getWriter())
                .rdate(review.getRdate())
                .title(review.getTitle())
                .content(review.getContent())
                .rating(review.getRating())
                .savedReviewFiles(review.getPReviewFiles()) // ReviewFile 리스트 설정
                .productId(review.getProduct().getProductId())
                .product(productDTO) // ProductDTO 설정
                .build();
    }


    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", writer='" + writer + '\'' +
                ", rdate=" + rdate +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", rating='" + rating + '\'' +
                // Exclude pReviewFiles and product to avoid recursion
                '}';
    }
}
