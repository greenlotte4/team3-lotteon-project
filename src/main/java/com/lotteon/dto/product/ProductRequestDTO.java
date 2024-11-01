package com.lotteon.dto.product;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lotteon.entity.product.OptionGroup;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO{

    private int firstLevelCategory;  // 추가된 필드
    private int secondLevelCategory; // 추가된 필드
    private int thirdLevelCategory;

    private String productName;
    private String productDesc;
    private String productCode;
    private List<String> optionName;
    private List<String> optionDesc;
    private List<Integer> optionStock;

    private String madeIn;
    private String sellerId;
    private long price;
    private int discount;
    private long stock;
    private int shippingFee;
    private int shippingTerms;
    private String rdate;
    private int point;

    private List<MultipartFile> files;

    private String condition;
    private String tax;
    private String receiptIssuance;
    private String busniesstype;
    private String manufactureCountry;

    private List<OptionGroupDTO> options;
    private List<ProductOptionCombinationDTO> combinations;
    private List<Map<String, Long>> stockMap;

}
