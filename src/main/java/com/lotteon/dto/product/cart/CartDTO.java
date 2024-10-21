package com.lotteon.dto.product.cart;

import lombok.*;
import org.springframework.data.annotation.Id;

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
    private int uid ;
    private LocalDateTime rdate;

    private List<CartItemDTO> items;


}
