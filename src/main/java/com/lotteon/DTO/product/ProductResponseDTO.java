package com.lotteon.dto.product;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

    private ProductDTO product;
    private ProductRequestDTO productRequest;
    private OptionDTO option;




}
