package com.lotteon.entity.admin;


import com.lotteon.entity.User.Seller;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.w3c.dom.Text;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    private String couponId; // 쿠폰번호

    private String couponName; // 쿠폰명
    private String couponType; // 쿠폰종류
    private String benefit; // 혜택
    private LocalDate startDate; // 시작 날짜
    private LocalDate endDate; // 종료 날짜
    private String notes; // 유의사항

    private String sellerCompany;
    private int issuedCount; // 발급수
    private int usedCount; // 사용수
    private String status; // 상태
    private LocalDate rdate; // 발급일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @JsonIgnore
    private Seller seller; // 발급자


}
