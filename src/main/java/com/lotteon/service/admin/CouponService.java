package com.lotteon.service.admin;

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.repository.admin.CouponRepository;
import com.lotteon.repository.user.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponService {

    private final CouponRepository couponRepository;
    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;


    public String randomCouponId(){
        return UUID.randomUUID().toString().replaceAll("-","").substring(0,10);
    }


    public void insertCoupon(CouponDTO couponDTO) {
        // 현재 인증된 사용자의 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUserUid  = authentication.getName(); // 로그인한 사용자의 username을 가져옴 (Seller의 식별자로 사용)

        log.info("Logged in seller username: " + loggedInUserUid );

        // 로그인한 셀러 정보를 조회
        Seller seller = sellerRepository.findByUserUid(loggedInUserUid )
                .orElseThrow(() -> new RuntimeException("Seller not found for username: " + loggedInUserUid ));

        Coupon coupon = Coupon.builder()
                .couponId(randomCouponId())
                .couponName(couponDTO.getCouponName())
                .couponType(couponDTO.getCouponType())
                .benefit(couponDTO.getBenefit())
                .startDate(couponDTO.getStartDate())
                .endDate(couponDTO.getEndDate())
                .notes(couponDTO.getNotes())
                .issuedCount(0) // 발급수 초기화
                .usedCount(0) // 사용수 초기화
                .status("발급 중") // 기본 상태 설정 (필요에 따라 수정)
                .rdate(LocalDate.now()) // 현재 날짜로 발급일 설정
                .sellerCompany(seller.getCompany())
                .build();

        log.info("Coupon------------------------" + coupon);

        couponRepository.save(coupon);
        log.info("Coupon inserted: " + coupon);
    }

    public CouponDTO selectCoupon(String couponId){

        Optional<Coupon> optCoupon = couponRepository.findById(couponId);

        if(optCoupon.isPresent()){
            Coupon coupon = optCoupon.get();
            log.info("Coupon selected: " + coupon);

            return modelMapper.map(coupon, CouponDTO.class);
        } else {
            return null;
        }

    }

    public CouponDTO endCoupon(String couponId){
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("Coupon could not be found for id: " + couponId));

        if("발급 중".equals(coupon.getStatus())||"발급 가능".equals(coupon.getStatus())){
            coupon.setStatus("종료됨");
            couponRepository.save(coupon);
            log.info("Coupon ended: " + coupon);

            return modelMapper.map(coupon, CouponDTO.class);
        } else {
            throw new RuntimeException("궁시렁궁시렁 오류남");
        }
    }
    // 페이징 기능 추가
    public Page<CouponDTO> selectCouponsPagination(String userUid, Pageable pageable) {
        // 셀러 정보를 가져오기 위해 UserUid로 셀러 조회
        Optional<Seller> seller = sellerRepository.findByUserUid(userUid); // 셀러 리포지토리 사용

        // 셀러가 없을 경우 처리 (예: null 체크)
        if (seller == null) {
            throw new RuntimeException("셀러 정보를 찾을 수 없습니다."); // 예외 처리 추가
        }

        // 등급 체크
        boolean adminCheck = "admin".equals(seller.get().getGrade()); // grade가 "admin"인지 확인

        // 쿠폰 리스트 조회
        Page<CouponDTO> couponPage = couponRepository.findCoupons(userUid, pageable);
        log.info("Admin check for UID {}: {}", userUid, adminCheck);
        return couponPage.map(coupon -> modelMapper.map(coupon, CouponDTO.class));
    }



}
