package com.lotteon.service.user;

import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.repository.admin.CouponIssuedRepository;
import com.lotteon.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponDetailsService {


    private final CouponIssuedRepository couponIssuedRepository;
    private final MemberRepository memberRepository;
    public List<CouponIssued> memberCouponList(Long memberId) {
        log.info("쿠폰디테일 서비스 요청");

        return couponIssuedRepository.findByMemberId(memberId);

    }
}
