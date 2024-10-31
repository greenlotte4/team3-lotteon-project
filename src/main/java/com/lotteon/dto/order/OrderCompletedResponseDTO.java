package com.lotteon.dto.order;

import com.lotteon.dto.User.SellerDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.entity.product.Product;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedResponseDTO {

    OrderDTO order;
    List<SellerDTO> sellers;


    @Builder
    public OrderCompletedResponseDTO(OrderDTO order, Set<SellerDTO> sellerDTOs) {
        // order.getOrderItems()가 null인지 확인하고 빈 리스트로 초기화
        List<OrderItemDTO> orderItems = order.getOrderItems() != null ? order.getOrderItems() : new ArrayList<>();
        this.order = order;
        this.sellers= new ArrayList<>();
        for(SellerDTO seller: sellerDTOs) {
            List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
            for(OrderItemDTO orderItem : orderItems) {
                if(seller.getUid().equals(orderItem.getSellerUid())){
                    orderItemDTOS.add(orderItem);
                }
            }
            seller.setOrderItems(orderItemDTOS);
            this.sellers.add(seller);
        }

    }



}
