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
    private String combinationName;
    private String optionCode;
    private Long stock;

    private Long  product;
}
