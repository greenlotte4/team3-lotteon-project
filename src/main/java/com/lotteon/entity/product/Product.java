package com.lotteon.entity.product;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
@Entity
@Table(name="product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productCode;
    private int categoryId;

    private String productName;
    private int price;
    private int stock;
    private int discount;
    private int shippingFee;
    private int shippingTerms; //무료배송 조건

    @CreationTimestamp
    private LocalDateTime rdate;
    private String ProductDesc; //상품설명

    @Builder.Default
    private int point=0;

    @Builder.Default
    private Boolean isCoupon =true;
    // 쿠폰 사용가능 유므
    @Builder.Default
    private Boolean isSaled = true; // 판매가능여부

    private String sellerId ;
    @Builder.Default
    private int sold=0; //판매량



    @PostPersist
    public void generateProductCode(){
        this.productCode = "C"+categoryId+"-P"+productId;
    }
}
