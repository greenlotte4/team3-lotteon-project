package com.lotteon.service.admin;

import com.lotteon.config.RedirectToLoginException;
import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.dto.admin.CouponIssuedDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
            couponName = " [전체상품 적용]" + couponName ;

            log.info("모든 상품에 대한 쿠폰이므로, ID가 없습니다.");
            CouponIssued couponIssued = CouponIssued.builder()
                    .issuanceNumber(rendomIssuedId())
                    .couponId(coupon.getCouponId())
                    .couponName(couponName)
                    .restrictions(coupon.getRestrictions())
                    .couponType(coupon.getCouponType())
                    .memberName(member.getName())
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
                    .restrictions(coupon.getRestrictions())
                    .couponType(coupon.getCouponType())
                    .memberName(member.getName())
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






    public void endCouponIssued(String couponIssuedId){}

    // 발급된 쿠폰 목록 조회
    public List<CouponIssued> couponIssuedList() {
        // 전체 발급된 쿠폰 목록 조회
        List<CouponIssued> couponIssuedList = couponIssuedRepository.findAll();
        log.info("조회된 쿠폰 목록: {}", couponIssuedList);  // 리스트가 잘 전달되는지 확인

        return couponIssuedList;
    }



}
