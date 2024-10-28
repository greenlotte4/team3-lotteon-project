package com.lotteon.entity.product;


import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.User.Seller;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private long categoryId;

    private String productName;
    private long price;
    private long stock;
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

    private long sellerNo;
    private String sellerId;

    @Builder.Default
    private int sold=0; //판매량

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @ToString.Exclude  // 외래키는 자식 테이블에 생성
    private List<ProductFile> files;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    @ToString.Exclude
    private List<Option> options;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="detail_id")
    @ToString.Exclude
    private ProductDetails productDetails;


    private int hit;


    private String file190;
    private String file230;
    private String file456;


    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Review> reviews = new ArrayList<>();

    @Transient
    private List<String> fileDescs = new ArrayList<>();
  
    //리뷰 별 평균 값
    private double productRating;



    @PostPersist
    public void generateProductCode(){
        this.productCode = "C"+categoryId+"-P"+productId;
    }


    public void setFiles(List<ProductFile> files) {
        this.files = files;
        if (files != null) {
            if (this.fileDescs == null) {
                this.fileDescs = new ArrayList<>();
            }
            for (ProductFile file : files) {
                switch (file.getType()) {
                    case "190" -> this.file190 = file.getSName();
                    case "230" -> this.file230 = file.getSName();
                    case "456" -> this.file456 = file.getSName();
                    default -> this.fileDescs.add(file.getSName());
                }
            }
        }
    }

    public void setOptions(List<Option> options) {
        this.options=options;
    }

    public void setProductDetails(ProductDetails productDetails) {
        this.productDetails=productDetails;
    }

    public ProductDTO toDTO(Product product) {
        ProductDTO dto = ProductDTO.builder()
                .productId(product.getProductId())
                .productCode(product.getProductCode())
                .categoryId(product.getCategoryId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(product.getStock())
                .discount(product.getDiscount())
                .shippingFee(product.getShippingFee())
                .shippingTerms(product.getShippingTerms())
                .hit(product.getHit())
                .ProductDesc(product.getProductDesc())
                .file190(product.getFile190())
                .file230(product.getFile230())
                .file456(product.getFile456())
                .point(product.getPoint())
                .isCoupon(product.getIsCoupon())
                .isSaled(product.getIsSaled())
                .productDetails(product.getProductDetails())
                .rdate(String.valueOf(product.getRdate()))
                .build();
        return dto;
    }

}
