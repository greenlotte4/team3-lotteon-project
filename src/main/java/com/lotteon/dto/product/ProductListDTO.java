package com.lotteon.dto.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductListDTO {

    private long productId;

    private long categoryId;

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

    private String file190;
    private String file230;
    private String file456;
    private List<String> filedesc;

    private List<ProductFileDTO> productFiles;

    public void setFiles(List<ProductFileDTO> productFiles){
        for(ProductFileDTO productFileDTO : productFiles){
            if (productFileDTO.getType().equals("190")){
                this.file190=productFileDTO.getSName();
            }else if (productFileDTO.getType().equals("230")){
                this.file230=productFileDTO.getSName();
            }else if (productFileDTO.getType().equals("456")){
                this.file456=productFileDTO.getSName();
            }else{
                filedesc.add(productFileDTO.getSName());
            }
        }
    }
}
