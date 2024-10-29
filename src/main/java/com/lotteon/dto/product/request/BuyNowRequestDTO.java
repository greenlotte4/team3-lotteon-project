package com.lotteon.dto.product.request;


import com.lotteon.entity.User.Member;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyNowRequestDTO {

    private String productId;
    private String productName;
    private String originalPrice;
    private String finalPrice;
    private int quantity;
    private String file190;
    private String optionId;
    private String optionName;
    private String point;

    private Member member;

}