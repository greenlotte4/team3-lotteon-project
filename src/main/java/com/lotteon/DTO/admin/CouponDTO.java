package com.lotteon.dto.admin;


import com.lotteon.dto.User.SellerDTO;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CouponDTO {
    private int couponId;
    private String couponName;
    private String couponType;
    private String benefit; // 혜택
    private String startDate; // 시작 날짜
    private String endDate; // 종료 날짜
    private String notes; // 유의사항


    private SellerDTO sellerDTO;
}
