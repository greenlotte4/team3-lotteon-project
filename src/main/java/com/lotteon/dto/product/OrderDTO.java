package com.lotteon.dto.product;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class OrderDTO {

    private long orderId;
    private long productId;
    private String uid;
    private long totalqty;
    private long totalPrice;
    private long totalShippingFee;
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
    private long inputPoint;
    //쿠폰 사용유무
    private boolean isCoupon;
    private long couponId;


}
