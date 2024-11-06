package com.lotteon.service.admin;

import com.lotteon.dto.admin.CouponIssuedDTO;
import com.lotteon.dto.admin.CouponListRequestDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.admin.CouponIssuedRepository;
import com.lotteon.repository.admin.CouponRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class CouponIssuedService {

    private final MemberRepository memberRepository;
    private final CouponIssuedRepository couponIssuedRepository;
    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    public String rendomIssuedId(){
        String issuedId;
        do{
            issuedId = UUID.randomUUID().toString().replaceAll("-","").substring(0,10);
        }while (couponRepository.existsById(issuedId));
        return issuedId;
    }
    public List<String> getUserRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }
    public void useCoupon(String issuanceNumber){
        Optional<CouponIssued> couponIssuedOpt = couponIssuedRepository.findById(issuanceNumber);

        if(couponIssuedOpt.isPresent()){
            CouponIssued couponIssued = couponIssuedOpt.get();


            // 이미 사용된 쿠폰인지 체크 (중복 사용 방지)
            if ("사용됨".equals(couponIssued.getUsageStatus())) {
                log.warn("이미 사용된 쿠폰입니다. 쿠폰번호: {}", issuanceNumber);
                throw new RuntimeException("이 쿠폰은 이미 사용되었습니다.");
            }

            // 쿠폰 사용 상태로 업데이트
            couponIssued.setUsageStatus("사용됨"); // "사용완료" 로 상태 변경
            couponIssued.setStatus("사용불가"); // 상태를 "사용불가"로 설정

            couponIssued.setUsedDateIfApplicable(); // 현재 시간으로 설정
            couponIssuedRepository.save(couponIssued);

            Coupon coupon = couponIssued.getCoupon();
            coupon.setUsedCount((coupon.getUsedCount() + 1));
            couponRepository.save(coupon);

            log.info("쿠폰 사용 처리 완료, 쿠폰번호 :{}", issuanceNumber);
        } else {
            log.warn("사용할 수 없는 쿠폰입니다. 쿠폰번호: {}", issuanceNumber);

        }

}

    public void insertCouponIssued(Member member, Coupon coupon, Product product) {

        log.info("쿠폰 서비스까진 들어왓따");
        String couponName = coupon.getCouponName();
        if (product == null || coupon.getProduct() == null) {

            log.info("모든 상품에 대한 쿠폰이므로, ID가 없습니다.");
            CouponIssued couponIssued = CouponIssued.builder()
                    .issuanceNumber(rendomIssuedId())
                    .couponId(coupon.getCouponId())
                    .couponName(couponName)
                    .startDate(coupon.getStartDate())
                    .endDate(coupon.getEndDate())
                    .benefit(coupon.getBenefit())
                    .restrictions(coupon.getRestrictions())
                    .couponType(coupon.getCouponType())
                    .memberName(member.getName())
                    .sellerCompany(coupon.getSellerCompany())
                    .member(member)
                    .productId(null) // "전체상품"을 나타내는 값
                    .build();
        log.info("널인 쿠폰 저장되는 값들" + couponIssued);
        couponIssuedRepository.save(couponIssued); // 엔티티 저장
        coupon.setIssuedCount(coupon.getIssuedCount() + 1);
        couponRepository.save(coupon);
    } else{
            // 특정 상품에 대한 쿠폰 처리
            CouponIssued couponIssued = CouponIssued.builder()
                    .issuanceNumber(rendomIssuedId())
                    .couponId(coupon.getCouponId())
                    .couponName(coupon.getCouponName())
                    .startDate(coupon.getStartDate())
                    .endDate(coupon.getEndDate())
                    .benefit(coupon.getBenefit())
                    .restrictions(coupon.getRestrictions())
                    .couponType(coupon.getCouponType())
                    .memberName(member.getName())
                    .sellerCompany(coupon.getSellerCompany())
                    .productName(product.getProductName())
                    .member(member)
                    .productId(coupon.getProduct().getProductId())
                    .build();

            log.info("아이디가 있는 쿠폰 저장되는 값들: " + couponIssued);
            couponIssuedRepository.save(couponIssued);

            coupon.setIssuedCount(coupon.getIssuedCount() + 1);
            couponRepository.save(coupon);
        }

    }






    public CouponIssuedDTO endCouponIssued(String issuanceNumber){
        CouponIssued couponIssued = couponIssuedRepository.findById(issuanceNumber)
                .orElseThrow(() -> new RuntimeException("Coupon could not be found for id: " + issuanceNumber));

        if ("사용가능".equals(couponIssued.getStatus())) {
            couponIssued.setStatus("종료됨");
            couponIssuedRepository.save(couponIssued);
            log.info("Coupon ended: " + issuanceNumber);
            return modelMapper.map(couponIssued, CouponIssuedDTO.class); // couponIssued 객체를 DTO로 변환하여 반환
        } else {
            throw new RuntimeException("궁시렁궁시렁 오류남");
        }
    }

    // 발급된 쿠폰 목록 조회
    public List<CouponIssued> couponIssuedList() {

        // 전체 발급된 쿠폰 목록 조회
        List<CouponIssued> couponIssuedList = couponIssuedRepository.findAll();
        log.info("조회된 쿠폰 목록: {}", couponIssuedList);  // 리스트가 잘 전달되는지 확인

        return couponIssuedList;
    }
    // 페이징 기능 추가
    public Page<CouponIssuedDTO> selectIssuedCouponsPagination(CouponListRequestDTO request, Long sellerId, Pageable pageable) {

        List<String> roles = getUserRoles();
        // 쿠폰 페이지 조회
        Page<CouponIssued> couponPage = null;

        if (roles.contains("ROLE_ADMIN")) {
            couponPage = couponIssuedRepository.findAll(pageable);
//            log.info("관리자 쿠폰: " + couponPage);
        } else if (roles.contains("ROLE_SELLER")) {
            // 일반 셀러는 자신의 쿠폰만 조회
            couponPage = couponIssuedRepository.findByCoupon_Seller_Id(sellerId, pageable);
            log.info("셀러 발급 쿠폰: " + couponPage);
            log.info("셀러 아이디 : " + sellerId );
        }

        int total = (int) Objects.requireNonNull(couponPage).getTotalElements();
        int totalPages = (int) Math.ceil((double) total / request.getSize());
        log.info("총 쿠폰 수: {}, 총 페이지 수: {}", total, totalPages);

        return couponPage.map(couponIssued -> modelMapper.map(couponIssued, CouponIssuedDTO.class));
    }





}
