package com.lotteon.dto.product.cart;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartRequestDTO {

    private long productId;
    private int quantity;
    private int optionId;
}
