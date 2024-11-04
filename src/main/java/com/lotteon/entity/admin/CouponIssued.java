package com.lotteon.entity.admin;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.User;
import com.lotteon.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "couponIssued")
public class CouponIssued {

    @Id
    private String issuanceNumber;

    private String couponId;
    private String couponType;
    private String couponName;
    private String memberName;
    private String productName;
    private Long productId;

    @Builder.Default
    private String usageStatus = "미사용"; // 기존 UsageStatus -> usageStatus

    @CreationTimestamp
    private LocalDateTime usageDate; // 기존 UsageDate -> usageDate


    @Builder.Default
    private String status = "발급중";


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "coupon_couponId")
    @JsonIgnore
    @JsonBackReference
    private Coupon coupon;


}
