package com.lotteon.entity.product;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    @Setter
    private String sellerId ;
    @Builder.Default
    private int sold=0; //판매량

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @ToString.Exclude  // 외래키는 자식 테이블에 생성
    private List<ProductFile> files;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    @ToString.Exclude
    private Set<Option> options;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    @ToString.Exclude
    private ProductDetails productDetails;

    private int hit;


    @PostPersist
    public void generateProductCode(){
        this.productCode = "C"+categoryId+"-P"+productId;
    }



}
