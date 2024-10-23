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

    private String couponId; // 쿠폰번호
    private String couponName; // 쿠폰명
    private String couponType; // 쿠폰종류
    private String benefit; // 혜택
    private LocalDate startDate; // 사용기간 시작
    private LocalDate endDate; // 사용기간 종료
    private int issuedCount; // 발급수
    private int usedCount; // 사용수
    private String status; // 상태
    private String notes;
    private LocalDate rdate; // 발급일

    private String sellerCompany; // 발급자
}
