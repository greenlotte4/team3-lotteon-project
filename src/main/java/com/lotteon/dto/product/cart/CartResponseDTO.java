package com.lotteon.dto.product.cart;


import com.lotteon.entity.cart.CartItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDTO {

    private List<CartItem> cartItems;
}
