package com.lotteon.dto.order;

import com.lotteon.dto.product.request.BuyNowRequestDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private OrderDTO order;
    private List<OrderItemDTO> orderItems;
    private long usePoint;
    private long couponResult;


    @Builder
    public  OrderResponseDTO(OrderRequestDTO orderRequestDTO) {

        List<BuyNowRequestDTO> products = orderRequestDTO.getProductDataArray();

        this.usePoint=orderRequestDTO.getUsedPointResult();
        this.couponResult=orderRequestDTO.getUsedCouponResult();


        this.orderItems = new ArrayList<>();
        for (BuyNowRequestDTO buyNowRequestDTO : products) {

            long originalQuantity = buyNowRequestDTO.getQuantity();
            long originalPrice = parseLongOrDefault(buyNowRequestDTO.getOriginalPrice(), 0);

            OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                    .price(originalPrice)
                    .productId(parseLongOrDefault(buyNowRequestDTO.getProductId(), 0))
                    .optionId(parseLongOrDefault(buyNowRequestDTO.getOptionId(), 0))
                    .optionDesc(buyNowRequestDTO.getOptionName())
                    .savedPrice(Long.parseLong(buyNowRequestDTO.getOriginalPrice()))
                    .orderPrice(Long.parseLong(buyNowRequestDTO.getFinalPrice()))
                    .savedDiscount(Long.parseLong(buyNowRequestDTO.getDiscount()))
                    .shippingTerms(Long.parseLong(buyNowRequestDTO.getShippingTerms()))
                    .stock(originalQuantity)
                    .point(parseLongOrDefault(buyNowRequestDTO.getPoint(),0))
                    .build();

            this.orderItems.add(orderItemDTO);

        }

        this.order = OrderDTO.builder()
                .addr1(orderRequestDTO.getAddr1())
                .addr2(orderRequestDTO.getAddr2())
                .hp(orderRequestDTO.getHp())
                .couponId(orderRequestDTO.getCouponId())
                .isCoupon(orderRequestDTO.getCouponId() > 0)
                .expectedPoint(parseLongOrDefault(orderRequestDTO.getFinalOrderPoint(), 0))
                .postcode(orderRequestDTO.getPostcode())
                .receiver(orderRequestDTO.getReceiver())
                .shippingInfo(orderRequestDTO.getShippingInfo())
                .totalOriginalPrice(parseLongOrDefault(orderRequestDTO.getTotalOriginalPrice(), 0))
                .totalDiscount(parseLongOrDefault(orderRequestDTO.getTotalDiscount(), 0))
                .usedPoint(orderRequestDTO.getUsedPointResult())
                .totalQuantity(parseLongOrDefault(orderRequestDTO.getTotalOrderQuantity(), 0))
                .totalShipping(parseLongOrDefault(orderRequestDTO.getTotalShippingFee(), 0))
                .couponDiscount(orderRequestDTO.getUsedCouponResult())
                .pay(orderRequestDTO.getCredit())
                .postcode(orderRequestDTO.getPostcode())
                .totalPrice(parseLongOrDefault(orderRequestDTO.getTotalFinalPrice(), 0))
                .totalShipping(parseLongOrDefault(orderRequestDTO.getTotalShippingFee(), 0))
                .build();


    }

    private long parseLongOrDefault(String value, long defaultValue) {
        try {
            return value != null ? Long.parseLong(value.replaceAll(",", "")) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


}
