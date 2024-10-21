package com.lotteon.dto.product;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class ProductDTO {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID productId;

    private int category;
}
