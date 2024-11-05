package com.lotteon.dto.admin;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssuedDTO {

    private String issuanceNumber;
    private String couponId;
    private String couponType;
    private String couponName;
    private String memberName; // 등록한 쿠폰을 발급한 맴버 이름
    private String productName;
    private String UsageStatus;
    private String UsageDate;
    private String status;
    private Long productId; // 등록된 상품 아이디
    private int restrictions;

    private List<CouponDTO> couponDTO;

}
