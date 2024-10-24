package com.lotteon.dto.product.cart;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {

    private int cartId;
    private String uid ;
    private LocalDateTime rdate;

    private List<com.lotteon.dto.product.cart.CartItemDTO> items;


}

