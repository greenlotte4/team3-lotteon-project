package com.lotteon.dto.product;

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
    private long optionId;
    private long stock;
    private long price;
    private String traceNumber;

}
