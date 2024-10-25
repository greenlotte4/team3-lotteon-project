package com.lotteon.service.admin;

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.dto.admin.CouponListRequestDTO;
import com.lotteon.dto.admin.CouponPageDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.repository.admin.CouponRepository;
import com.lotteon.repository.user.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
        Seller seller = sellerRepository.findByUserUid(loggedInUserUid)
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
                .seller(seller)
                .build();

        log.info("Coupon------------------------" + coupon);

        couponRepository.save(coupon);
        log.info("Coupon inserted: " + coupon);
    }

    // 쿠폰 상세 조회
    public CouponDTO selectCoupon(String couponId) {
        return couponRepository.findById(couponId)
                .map(coupon -> {
                    log.info("Coupon selected: " + coupon);
                    return modelMapper.map(coupon, CouponDTO.class);
                })
                .orElse(null);
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
    public Page<CouponDTO> selectCouponsPagination(CouponListRequestDTO request,String grade,long sellerId) {
        // 요청 DTO에서 페이지 정보와 사용자 UID 추출
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0; // 1-based to 0-based
        int size = request.getSize();
        Pageable pageable = PageRequest.of(page, size); // Pageable 생성


        // 쿠폰 페이지 조회
        Page<Coupon> couponPage = null;
        if(grade.contains("ADMIN")){
            couponPage = couponRepository.findAll(pageable);
            log.info("관리자 쿠폰: " + couponPage);
        }else if(grade.contains("SELLER")){
            // 일반 셀러는 자신의 쿠폰만 조회
            couponPage = couponRepository.findBySellerId(sellerId, pageable);
            log.info("일반인 쿠폰: " + couponPage);
        }


        
        log.info("요청된 셀러 ID: {}", request.getSellerId());


        

        return couponPage.map(coupon -> modelMapper.map(coupon, CouponDTO.class));
    }
//    // 모든 쿠폰 조회 메소드
//    public Page<CouponPageDTO> selectCouponsPagination(CouponListRequestDTO requestDTO, Pageable pageable) {
//        return couponRepository.selectCouponByUserIdForList(Seller.getId(), pageable);
//    }
//
//    // 검색 기능을 위한 메소드
//    public Page<CouponPageDTO> searchCoupons(String uid, Pageable pageable, String searchType, String searchValue) {
//        return couponRepository.searchCoupons(uid, pageable, searchType, searchValue);
//    }
    // 검색 기능
    public Page<CouponDTO> searchCoupons(long sellerId, String searchType, String searchValue, Pageable pageable) {
        Page<CouponPageDTO> couponPage = couponRepository.searchCoupons(sellerId, pageable, searchType, searchValue);
        return couponPage.map(coupon -> modelMapper.map(coupon, CouponDTO.class));
    }


}
