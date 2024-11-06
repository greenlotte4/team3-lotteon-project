package com.lotteon.controller;

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.admin.CouponRepository;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.service.admin.CouponIssuedService;
import com.lotteon.service.admin.CouponService;
import com.lotteon.service.product.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/coupon")
public class CouponApiController {

    private final CouponIssuedService couponIssuedService;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final CouponService couponService;
    private final ModelMapper modelMapper;
    @GetMapping("/{productId}")
    public ResponseEntity<List<CouponDTO>> getCouponsForProduct(@PathVariable Long productId) {
        log.info("쿠폰 리스트 불러오는거 요청 왔따 : " + productId);
        if (productId == null) {
            return ResponseEntity.ok(Collections.emptyList()); // 빈 리스트 반환
        }

        log.info("상품 ID: {}", productId);

        // 상품 ID에 해당하는 쿠폰을 조회하는 로직
        List<Coupon> coupons = couponService.selectCouponIssued(productId);

        // 상품 ID에 해당하는 쿠폰이 없을 경우 빈 리스트 반환
        if (coupons == null || coupons.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList()); // 빈 리스트 반환
        }
        // Coupon 리스트를 CouponDTO 리스트로 변환
        List<CouponDTO> couponDTOs = coupons.stream()
                .map(coupon -> modelMapper.map(coupon, CouponDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(couponDTOs);
    }

    @GetMapping("/all/coupons")
    public ResponseEntity<List<CouponDTO>> getAllCoupons() {
        log.info("쿠폰 전체 리스트 불러오는거 요청 왔따");

        List<Coupon> coupons = couponService.selectCouponIssued(null);
        // Coupon 리스트를 CouponDTO 리스트로 변환
        List<CouponDTO> couponDTOs = coupons.stream()
                .map(coupon -> modelMapper.map(coupon, CouponDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(couponDTOs);
    }

    @PostMapping("/apply/{couponId}")
    public ResponseEntity<CouponDTO> applyCoupon(@PathVariable("couponId") String couponId, Principal principal) {
        log.info("쿠폰 발금 버튼 클릭되서 요청왔다");

        String userUid = principal.getName();

        Member member = memberRepository.findByUser_Uid(userUid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        log.info("사용자 가 누군가"+ member);

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("쿠폰을 찾을 수 없습니다."));
        log.info("어떤 쿠폰인가 "+coupon);
        Product product = coupon.getProduct();

        couponIssuedService.insertCouponIssued(member, coupon, product);

        couponIssuedService.useCoupon(couponId);


        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setCouponId(coupon.getCouponId());
        couponDTO.setCouponName(coupon.getCouponName());
        couponDTO.setCouponType(coupon.getCouponType());

        return ResponseEntity.ok(couponDTO);
    }
}
