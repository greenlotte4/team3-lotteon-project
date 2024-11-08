package com.lotteon.dto.order;

import com.lotteon.entity.order.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class OrderWithGroupedItemsDTO {

    private long orderId;
    private String uid;
    private LocalDateTime orderDate;
    private List<OrderItemDTO> groupedOrderItems;

    public OrderWithGroupedItemsDTO(Order order) {
        this.orderId = order.getOrderId();
        this.orderDate = order.getOrderDate();
        this.groupedOrderItems = order.getOrderProducts().stream()
                .map(item -> new OrderItemDTO(item))
                .collect(Collectors.toList());
    }
}
