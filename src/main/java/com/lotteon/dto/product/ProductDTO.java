package com.lotteon.dto.product;


import com.lotteon.dto.User.SellerDTO;
import com.lotteon.entity.product.ProductDetails;
import com.lotteon.entity.product.Review;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductDTO{

    private Long productId;

    private Long categoryId;
    private Long sellerNo;
    private String productName;
    private Long price;
    private Long stock;
    private int discount;
    private int shippingFee;
    private int shippingTerms; //무료배송 조건
    private String rdate;
    private String productDesc; //상품설명
    private int point;
    private Boolean isCoupon; // 쿠폰 사용가능 유므
    private Boolean isSaled; // 판매가능여부
    private String sellerId;
    private String productCode;
    private Long hit;
    private String file190;
    private String file230;
    private String file456;
    private List<String> filedesc = new ArrayList<>();;

    //변경된 OptionDTO
    private List<OptionGroupDTO> optionGroups;

    private Long sold=0L;


    private List<ProductFileDTO> productFiles;
    private List<OptionDTO> options;
    private ProductDetailsDTO productDetails;
    private SellerDTO seller;

    //main list 판매자회사이름
    private String company;
    private double productRating;

    //리뷰 추가
    private List<Review> reviews;
    private List<ReviewDTO> reviewDTOs;

    private List<ProductOptionCombinationDTO> optionCombinations;


    public void addFileDescriptions(List<String> files) {
        if (files != null && !files.isEmpty()) {
            this.filedesc.addAll(files);
        }
    }


}
