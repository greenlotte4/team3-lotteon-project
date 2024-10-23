package com.lotteon.service.admin;

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.repository.admin.CouponRepository;
import com.lotteon.repository.user.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
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


    public List<CouponDTO> selectCouponAll(){

        List<Coupon> coupons = couponRepository.findAll();
        List<CouponDTO> couponDTOs = new ArrayList<>();

        for (Coupon coupon : coupons) {
            CouponDTO couponDTO = modelMapper.map(coupon, CouponDTO.class);
            couponDTOs.add(couponDTO);
        }
        return couponDTOs;
    }

}
