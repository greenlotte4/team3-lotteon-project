package com.lotteon.dto.product;

import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.Review;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Log4j2

public class ProductSummaryDTO  {
    private Long categoryId;
    private Long productId;
    private String productName;
    private Long price;
    private int discount;
    private int shippingFee;
    private int shippingTerms; //무료배송 조건
    private String productDesc; //상품설명
    private String file230;
    private String file190;
    private Long sellerId;
    private String sellerName;
    private String company;
    private List<Review> reviews;
    private Double rating;
    private long discountPrice;
    private long finalPrice;


    @Builder
    @QueryProjection
    public  ProductSummaryDTO(Long categoryId,Long productId, String productName, Long price, int discount, int shippingFee, int shippingTerms, String productDesc, String file230, String file190, Long sellerId, String sellerName, String company) {
        this.categoryId = categoryId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.discount = discount;
        this.shippingFee = shippingFee;
        this.shippingTerms = shippingTerms;
        this.productDesc = productDesc;
        this.file230 = file230;
        this.file190 = file190;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.company = company;

        this.discountPrice = Math.round(price * (100 - discount) / 100.0 / 100) * 100;
        this.finalPrice =price- Math.round(price * (100 - discount) / 100.0 / 100) * 100;

    }

    public Double setRating(List<Review> reviews) {

        if (reviews == null || reviews.isEmpty()) return 0.0;
        return reviews.stream()
                .map(Review::getRating)              // `rating` 값을 가져옴
                .filter(Objects::nonNull)               // `null` 값 제외
                .mapToDouble(Double::parseDouble)       // `String`을 `Double`로 변환
                .average()
                .orElse(0.0);

    }

    public Double getAverageRating() {
        if (reviews == null || reviews.isEmpty()) return 0.0;
        return reviews.stream()
                .map(Review::getRating)              // `rating` 값을 가져옴
                .filter(Objects::nonNull)               // `null` 값 제외
                .mapToDouble(Double::parseDouble)       // `String`을 `Double`로 변환
                .average()
                .orElse(0.0);                           // 평균이 없으면 0.0 반환
    }
}
