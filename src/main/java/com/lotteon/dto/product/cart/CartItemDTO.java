package com.lotteon.dto.product.cart;


import com.lotteon.dto.product.ProductDTO;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {

    private int cartItemId;
    private int stock;
    private int price;

    private ProductDTO productDTO;
    private int total;
}
