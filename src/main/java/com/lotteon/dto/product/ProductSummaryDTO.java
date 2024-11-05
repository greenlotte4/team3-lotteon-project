package com.lotteon.dto.product;

import com.lotteon.entity.product.Review;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Log4j2

public class ProductSummaryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @JsonIgnore
    private List<Review> reviews;
    private Double rating = 0.0; // rating이 null일 경우 기본값 설정
    private long discountPrice;
    private long finalPrice;
    private double productRating;


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

        this.discountPrice = Math.round(price * discount / 100.0 / 100) * 100;
        this.finalPrice = price - this.discountPrice;

    }

    public void setRating(List<String> ratings) {
        this.rating = calculateAverageRating(ratings);
    }

    public double calculateAverageRating(List<String> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream()
                .filter(Objects::nonNull)
                .mapToDouble(rating -> {
                    try {
                        return Double.parseDouble(rating);
                    } catch (NumberFormatException e) {
                        log.warn("Failed to parse rating: " + rating);
                        return 0.0;
                    }
                })
                .average()
                .orElse(0.0);
    }

}
