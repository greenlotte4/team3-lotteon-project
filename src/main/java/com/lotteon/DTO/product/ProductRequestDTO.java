package com.lotteon.dto.product;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    private int thirdLevelCategory;

    private String productName;
    private String productDesc; //상품설명
    private String option;
    private String optionId;
    private int optionStock;
    private String madeIn;
    private String sellerId;
    private int price;
    private int discount;
    private int stock;
    private int shippingFee;
    private int shippingTerms; //무료배송 조건
    private String rdate;
    private int point;

    private MultipartFile file190;
    private MultipartFile file230;
    private MultipartFile file456;
    private MultipartFile file940;

    private String condition;
    private String tax;
    private String receiptIssuance;
    //사업자구분
    private String busniesstype;

    //원산지
    private String manufactureCountry;

}
