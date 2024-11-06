package com.lotteon.dto.order;

public enum DeliveryStatus {
    ORDER_CONFIRMED("주문확인중"),
    PREPARING("상품준비중"),
    SHIPPING("상품배송중"),
    DELIVERED("상품배송완료"),
    RETURN_REQUESTED("반품요청중"),
    RETURN_PENDING("반품대기중"),
    REFUND_REQUESTED("환불요청중"),
    REFUND_PENDING("환불대기중"),
    REFUND_COMPLETED("환불완료");

    private final String description;

    DeliveryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}