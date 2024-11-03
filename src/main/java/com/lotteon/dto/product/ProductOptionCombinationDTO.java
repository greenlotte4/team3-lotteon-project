package com.lotteon.dto.product;


import com.lotteon.entity.product.Product;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class ProductOptionCombinationDTO {


    private Long combinationId;
    private String combination;
    private String optionCode;
    private Long productId; // 상품 id
    private String combination_name;
    private Long additionalPrice;
    private Long stock;
    private Long  product;
}
