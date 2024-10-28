package com.lotteon.controller;
/*
   날짜: 2024/10/25
   이름 : 최영진
   내용 : 쿠폰pagine

   추가내역
   -------------
   10.26  하진희 - 추가작업내역 작성
 */

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.dto.admin.CouponListRequestDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.repository.admin.CouponRepository;
import com.lotteon.security.MyUserDetails;
import com.lotteon.service.admin.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/seller/coupon")
public class AdminCouponController {

    private final CouponService couponService;
    private final CouponRepository couponRepository;


    @GetMapping("/list")
    public String adminCouponList(
            @ModelAttribute CouponListRequestDTO requestDTO,
            Model model) {

        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        log.info("userDetails  :: "+userDetails.getAuthorities());
        Seller seller = userDetails.getSeller();
        Long sellerId = seller.getId();
        String grade2 = userDetails.getAuthorities().toString();

        log.info("3324234234234234234 : "+grade2);
        model.addAttribute("seller", seller); // 셀러 정보를 모델에 추가
        model.addAttribute("sellerGrade", seller.getGrade());

        log.info("여기는???????!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        Page<CouponDTO> couponPage = couponService.selectCouponsPagination(requestDTO, grade2, seller.getId());

        // 등급에 따라 쿠폰 목록 조회
        if (grade2.contains("ADMIN")) {
            log.info("어드민!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            couponPage = couponService.selectCouponsPagination(requestDTO,grade2,0); // adminRequest를 사용하여 모든 쿠폰 조회
        } else if (grade2.contains("SELLER")) {
            couponPage = couponService.selectCouponsPagination(requestDTO,grade2,sellerId);
            log.info("else??????????!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        }

        model.addAttribute("couponList", couponPage.getContent());
        model.addAttribute("totalPages", couponPage.getTotalPages());
        model.addAttribute("currentPage", couponPage.getNumber());
        log.info("쿠폰 목록 조회 성공: {}", couponPage);
        log.info("등급!!!!!!!!!!!!!!!!"+seller.getGrade() );


        return "content/admin/coupon/list";
    }

    @GetMapping("/coupons")
    public String listCoupons(@RequestParam(defaultValue = "0") int page, Model model) {

        Pageable pageable = PageRequest.of(page, 10); // 한 페이지에 10개
        Page<Coupon> couponPage = couponRepository.findAll(pageable);

        log.info("호출됫따!!!!!!!!!!!!!!!!!!" + page);
        log.info("호출됫따!!!!!!!!!!!!!!!!!!");

        return "content/admin/coupon/list";  // Thymeleaf 뷰
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

    @PutMapping("/{couponId}/end")
    public ResponseEntity<CouponDTO> endCoupon(@PathVariable("couponId") String couponId) {
        CouponDTO updatedCoupon = couponService.endCoupon(couponId);

        log.info("---------쿠폰 상태------------"+updatedCoupon);
        return ResponseEntity.ok(updatedCoupon);

    }


    @GetMapping("/issued")
    public String adminIssuedModify(Model model) {

        return "content/admin/coupon/issued";
    }

}
