package com.lotteon.dto.product;


import com.lotteon.entity.product.ProductDetails;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductDTO{

    private Long productId;

    private int categoryId;

    private String productName;
    private int price;
    private int stock;
    private int discount;
    private int shippingFee;
    private int shippingTerms; //무료배송 조건
    private String rdate;
    private String ProductDesc; //상품설명
    private int point;
    private Boolean isCoupon; // 쿠폰 사용가능 유므
    private Boolean isSaled; // 판매가능여부
    private String sellerId;
    private String productCode;
    private int hit;

    private List<ProductFileDTO> productFiles;
    private List<OptionDTO> options;
    private ProductDetails productDetails;



}
