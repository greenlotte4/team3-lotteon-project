package com.lotteon.entity.product;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lotteon.dto.product.ProductOptionCombinationDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
@Entity
@Table(name="productOptionCombination")
public class ProductOptionCombination {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long combinationId;

    private String combination;
    private Long stock;
    private String optionCode;
    private Long additionalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;


    public ProductOptionCombinationDTO toDTO() {
        System.out.println("여기바로 그지점");
        
        return ProductOptionCombinationDTO.builder()
                .combinationId(this.combinationId)
                .combination(this.combination)
                .stock(this.stock)
                .optionCode(this.optionCode)
                .additionalPrice(this.additionalPrice)
                .build();
    }




}