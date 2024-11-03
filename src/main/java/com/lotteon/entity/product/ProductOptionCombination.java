package com.lotteon.entity.product;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lotteon.dto.product.ProductOptionCombinationDTO;
import jakarta.persistence.*;
import lombok.*;

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
    @JsonBackReference // Prevents cyclic serialization back to Product
    private Product product;


    public ProductOptionCombinationDTO toDTO() {
        return ProductOptionCombinationDTO.builder()
                .combinationId(this.combinationId)
                .combination(this.combination)
                .stock(this.stock)
                .optionCode(this.optionCode)
                .additionalPrice(this.additionalPrice)
                .build();
    }


}
