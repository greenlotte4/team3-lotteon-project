package com.lotteon.dto.order;


import com.lotteon.dto.User.SellerDTO;
import com.lotteon.entity.order.Order;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class OrderDTO {

    private long orderId;
    private String uid;
    private long totalOriginalPrice;
    private long totalPrice;
    private long totalQuantity;
    private long totalDiscount;
    private long totalShipping;
    private long expectedPoint;
    private String receiver;
    private String hp;
    private String postcode;
    private String addr1;
    private String addr2;
    private String shippingInfo;
    private long usedPoint;   //사용 포인트
    private LocalDateTime orderDate;
    private String orderStatus;
    private String pay;
    //쿠폰 사용유무
    private boolean isCoupon;
    private long couponId;
    private SellerDTO seller;
    private List<OrderItemDTO> orderItems;


    public Order toEntity() {
        return  Order.builder()
                .isCoupon(this.isCoupon)
                .orderId(this.orderId)
                .uid(this.uid)
                .totalOriginalPrice(this.totalOriginalPrice)
                .totalPrice(this.totalPrice)
                .totalQuantity(this.totalQuantity)
                .totalDiscount(this.totalDiscount)
                .totalShipping(this.totalShipping)
                .expectedPoint(this.expectedPoint)
                .receiver(this.receiver)
                .hp(this.hp)
                .postcode(this.postcode)
                .shippingInfo(this.shippingInfo)
                .usedPoint(this.usedPoint)
                .orderDate(this.orderDate)
                .orderStatus(this.orderStatus)
                .pay(this.pay)
                .build();
    }


}
