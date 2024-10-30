package com.lotteon.dto.order;

import com.lotteon.dto.product.ProductDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class OrderItemDTO {

    private long orderItemId;
    private OrderDTO order;
    private ProductDTO product;
    private long productId;
    private long orderId;
    private long optionId;
    private long stock;
    private long price;
    private String traceNumber;
    private String sellerUid;


}
