package com.lotteon.dto.order;


import com.lotteon.dto.product.cart.CartItemDTO;
import com.lotteon.entity.cart.CartItem;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartOrderRequestDTO {


    private long cartId;
    private List<CartItemDTO> cartItemDTO;





}





