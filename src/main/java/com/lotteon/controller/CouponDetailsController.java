package com.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/coupon/details")
public class CouponDetailsController {

    @GetMapping
    public String getCouponDetails() {
        // 단순히 couponDetail.html을 반환
        return "content/user/coupondetails"; // HTML 경로 (확장자 제외)
    }
}
