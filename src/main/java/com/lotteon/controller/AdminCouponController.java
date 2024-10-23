package com.lotteon.controller;


import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.security.MyUserDetails;
import com.lotteon.service.admin.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupon")
public class AdminCouponController {

    private final CouponService couponService;
    @GetMapping("/list")
    public String adminCouponList(Model model) {

        return "content/admin/coupon/list";
    }

    @GetMapping("/issued")
    public String adminIssuedModify(Model model) {

        return "content/admin/coupon/issued";
    }

    @PostMapping("/register")
    public String registerCoupon(@ModelAttribute CouponDTO couponDTO ,Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Seller seller = userDetails.getSeller();


        log.info("Received coupon data: {}", couponDTO);
        log.info("seller--------------------------"+seller);
        try {
            couponService.insertCoupon(couponDTO);
            model.addAttribute("massage","쿠폰이 등록되었스빈다..");

        } catch (Exception e) {
            model.addAttribute("error","등록에 실패했습니다."+ e.getMessage());
        }

        model.addAttribute("seller", seller);

        return "content/admin/coupon/list";
    }
}
