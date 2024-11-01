package com.lotteon.entity.product;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class ProductOptionCombination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long combinationId;
    private String combinationName;
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
