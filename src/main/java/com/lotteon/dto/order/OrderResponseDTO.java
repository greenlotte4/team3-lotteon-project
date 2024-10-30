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


    @Builder
    public  OrderResponseDTO(OrderRequestDTO orderRequestDTO) {

        List<BuyNowRequestDTO> products = orderRequestDTO.getProductDataArray();

        long totalPrice=0;
        long totalQuantity=0;
        long totalDiscount=0;
        long totalShipping=0;
        long expectedPoint=0;
        this.orderItems = new ArrayList<>();
        for (BuyNowRequestDTO buyNowRequestDTO : products) {

            long originalQuantity = buyNowRequestDTO.getQuantity();
            long originalPrice = parseLongOrDefault(buyNowRequestDTO.getOriginalPrice(), 0);

            OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                    .price(originalPrice)
                    .productId(parseLongOrDefault(buyNowRequestDTO.getProductId(), 0))
                    .optionId(parseLongOrDefault(buyNowRequestDTO.getOptionId(), 0))
                    .stock(originalQuantity)
                    .build();

            this.orderItems.add(orderItemDTO);

            totalQuantity += originalQuantity;
            totalPrice += originalPrice * originalQuantity;
            double discountAmount = (double) (parseLongOrDefault(buyNowRequestDTO.getOriginalPrice(), 0) * (100 - parseLongOrDefault(buyNowRequestDTO.getDiscount(), 0))) / 100;
            totalDiscount += (long) Math.floor(discountAmount);
            totalShipping += parseLongOrDefault(buyNowRequestDTO.getShippingFee(), 0);
            expectedPoint += parseLongOrDefault(buyNowRequestDTO.getPoint(), 0);
        }

        OrderDTO orderDTO = OrderDTO.builder()
                .receiver(orderRequestDTO.getReceiver())
                .hp(orderRequestDTO.getHp())
                .postcode(orderRequestDTO.getPostcode())
                .addr1(orderRequestDTO.getAddr1())
                .addr2(orderRequestDTO.getAddr2())
                .shippingInfo(orderRequestDTO.getShippingInfo())
                .expectedPoint(orderRequestDTO.getUsedPointResult())
                .pay(orderRequestDTO.getCredit())
                .usedPoint(orderRequestDTO.getUsedPointResult())
                .couponId(orderRequestDTO.getCouponId())
                .isCoupon(orderRequestDTO.getCouponId() > 0)
                .totalPrice(totalPrice)
                .totalQuantity(totalQuantity)
                .totalDiscount(totalDiscount)
                .totalShipping(totalShipping)
                .expectedPoint(expectedPoint)
                .build();
        this.order = orderDTO;

    }

    private long parseLongOrDefault(String value, long defaultValue) {
        try {
            return value != null ? Long.parseLong(value) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


}
