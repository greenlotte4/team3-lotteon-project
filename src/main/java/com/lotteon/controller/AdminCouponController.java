package com.lotteon.controller;


import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.security.MyUserDetails;
import com.lotteon.service.admin.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupon")
public class AdminCouponController {

    private final CouponService couponService;



    @GetMapping("/list")
    public String adminCouponList(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Seller seller = userDetails.getSeller();

        model.addAttribute("seller", seller); // 셀러 정보를 모델에 추가

        List<CouponDTO> couponList  = couponService.selectCouponAll();
        model.addAttribute("couponList", couponList );


        return "content/admin/coupon/list";
    }

    public String couponView(String  couponId, Model model) {

        CouponDTO couponDTO = couponService.selectCoupon(couponId);

        model.addAttribute("couponDTO", couponDTO);

        return "content/admin/coupon/list";
    }

    @GetMapping("/issued")
    public String adminIssuedModify(Model model) {

        return "content/admin/coupon/issued";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCoupon(@RequestBody CouponDTO couponDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        log.info("User details-----------------------: {}", userDetails);
        Seller seller = userDetails.getSeller();
        log.info("Seller from user details-----------------------: {}", seller);

        if(seller == null) {
            return ResponseEntity.badRequest().body("셀러 정보를 찾을 수 없습니다.");

        }

        try {
            couponService.insertCoupon(couponDTO);
            return ResponseEntity.ok("쿠폰이 등록되었습니다.");
        } catch (Exception e) {
            log.error("Coupon registration failed--------------------: {}", e.getMessage());
            return ResponseEntity.status(500).body("등록에 실패했습니다: " + e.getMessage());
        }
    }
}
