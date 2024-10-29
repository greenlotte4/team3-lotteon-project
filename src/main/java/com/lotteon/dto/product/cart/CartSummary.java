package com.lotteon.dto.product.cart;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartSummary {

    private int finalTotalQuantity;
    private double finalTotalPrice;
    private double finalTotalDiscount;
    private double finalTotalDeliveryFee;
    private double finalTotalOrderPrice;
    private double finalTotalPoints;

}
