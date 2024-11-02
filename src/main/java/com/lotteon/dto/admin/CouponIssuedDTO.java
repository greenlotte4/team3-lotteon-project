package com.lotteon.dto.admin;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssuedDTO {

    private String IssuanceNumber;
    private String couponId;
    private String couponType;
    private String couponName;
    private String member;
    private String UsageStatus;
    private String UsageDate;
    private String status;
}
