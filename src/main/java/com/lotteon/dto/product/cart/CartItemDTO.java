
package com.lotteon.dto.product.cart;


import com.lotteon.dto.product.ProductDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    //추가
    private int cartItemId;
    private int stock;
    private long price;

    private String optionName;
    private ProductDTO productDTO;
    private int total;
}

