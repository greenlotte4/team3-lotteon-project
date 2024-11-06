package com.lotteon.service.user;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.repository.admin.CouponIssuedRepository;
import com.lotteon.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponDetailsService {


    private final CouponIssuedRepository couponIssuedRepository;
    private final MemberRepository memberRepository;


    public List<CouponIssued> memberCouponList(String uid) {
        log.info("쿠폰디테일 서비스 요청");

        Optional<Member> memberOtp = memberRepository.findByUid(uid);

        if (memberOtp.isEmpty()) {
            throw new RuntimeException("Member not found for uid: " + uid);
        }

        Member member = memberOtp.get();

        log.info("멤버 아이디"+member.getId());
        log.info("멤버 유아이디"+member.getUid());
        return couponIssuedRepository.findByMemberId(member.getId());
    }

    public List<CouponIssued> memberOrderCouponList(String uid, String productId) {
        log.info("쿠폰디테일 서비스 요청");

        Optional<Member> memberOtp = memberRepository.findByUid(uid);

        if (memberOtp.isEmpty()) {
            throw new RuntimeException("Member not found for uid: " + uid);
        }

        Member member = memberOtp.get();

        List<CouponIssued> couponIssuedList = couponIssuedRepository.findByMemberId(member.getId());

        List<CouponIssued> validCoupons = new ArrayList<>();
        for (CouponIssued couponIssued : couponIssuedList) {
            if (couponIssued.getProductId() == null || couponIssued.getProductId().equals(productId)) {
                validCoupons.add(couponIssued);
            }
        }


        return validCoupons;
    }
}
