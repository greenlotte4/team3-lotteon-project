package com.lotteon.dto.admin;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssuedRequestDTO {

    private String issuanceCouponId;
    private String usageStatus;
    private String status;

}
