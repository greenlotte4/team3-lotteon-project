package com.lotteon.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderWithGroupedItemsDTO {

    private long orderId;
    private String uid;
    private LocalDateTime orderDate;
    private List<SellerOrderItemDTO> groupedOrderItems;
}
