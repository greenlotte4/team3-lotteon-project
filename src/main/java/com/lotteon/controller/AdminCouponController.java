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
import com.lotteon.dto.admin.CouponListResponseDTO;
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
        if (requestDTO.getPage() < 1) {
            requestDTO.setPage(1);
        }
        // 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        Seller seller = userDetails.getSeller();

        List<String> userRoles = couponService.getUserRoles();

        Pageable pageable = requestDTO.getPageable(); // 정렬 기준 없이 호출

        model.addAttribute("seller", seller); // 셀러 정보를 모델에 추가
        model.addAttribute("sellerGrade", seller.getGrade());
        Page<CouponDTO> couponPage = couponService.selectCouponsPagination(requestDTO, seller.getId(), pageable);


        log.info("페이징: {}", pageable); // Pageable 로그 추가

        // CouponListResponseDTO 생성
        CouponListResponseDTO responseDTO = CouponListResponseDTO.builder()
                .couponDTOList(couponPage.getContent())
                .total((int) couponPage.getTotalElements())
                .pg(requestDTO.getPage()) // 요청 DTO에서 현재 페이지 번호 가져오기
                .size(requestDTO.getSize()) // 요청 DTO에서 페이지 크기 가져오기
                .build();

        log.info("요청 DTO: {}", responseDTO); // 응답 DTO 로그 추가

        // 페이지 정보 계산
        responseDTO.setStartNo(responseDTO.getTotal() - ((responseDTO.getPg() - 1) * responseDTO.getSize()));
        responseDTO.setStart(Math.max(1, responseDTO.getEnd() - (responseDTO.getSize() - 1)));
        responseDTO.setEnd(Math.min((int) (Math.ceil(responseDTO.getPg() / (double) responseDTO.getSize()) * responseDTO.getSize()), responseDTO.getTotal()));
        responseDTO.setPrev(responseDTO.getStart() > 1);
        responseDTO.setNext(responseDTO.getTotal() > responseDTO.getEnd());

        model.addAttribute("responseDTO", responseDTO);
        model.addAttribute("userRoles", userRoles);
        model.addAttribute("couponList", couponPage.getContent());
        model.addAttribute("totalPages", couponPage.getTotalPages());
        model.addAttribute("currentPage", couponPage.getNumber());

        return "content/admin/coupon/list";
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

    @GetMapping("/coupons")
    public String couponList(Model model) {

        return "content/admin/coupon/list";
    }
}
