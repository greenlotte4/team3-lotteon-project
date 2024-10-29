
package com.lotteon.dto.product.cart;


import com.lotteon.dto.product.OptionDTO;
import com.lotteon.dto.product.ProductDTO;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {

    private Long cartItemId;    // CartItem ID
    private int stock;          // 재고
    private long price;         // 가격
    private List<OptionDTO> option;      // 옵션 (예: 색상, 사이즈 등)
    private ProductDTO productDTO; // 상품 정보
    private int deliveryFee;

    private int quantity;       // 수량 (추가)
    private long total;         // 총 가격
    private int discount;       // 할인율 (선택 사항)
    private int points;         // 적립 포인트 (선택 사항)
}


