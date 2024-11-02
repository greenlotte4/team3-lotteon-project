package com.lotteon.entity.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.User;
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
    private String IssuanceNumber;

    private String couponId;
    private String couponType;
    private String couponName;
    private String memberName;

    @Builder.Default
    private String UsageStatus="미사용";
    @CreationTimestamp
    private LocalDateTime UsageDate;
    @Builder.Default
    private String status = "발급중";


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "coupon_couponId")
    @JsonIgnore
    private Coupon coupon;
}
