package com.lotteon.dto.admin;

import com.lotteon.dto.order.DeliveryStatus;
import com.lotteon.dto.order.OrderDTO;
import com.lotteon.dto.product.ProductDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class AdminOrderItemDTO {

    private long orderItemId;
    private String productName;
    private long orderId;
    private DeliveryStatus status;
    private long price;
    private long savedPrice;
    private long orderPrice;
    private long savedDiscount;
    private long shippingFees;
    private long stock;


}
