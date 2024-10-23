package com.lotteon.service.admin;

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.repository.admin.CouponRepository;
import com.lotteon.repository.user.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Log4j2
public class CouponService {

    private final CouponRepository couponRepository;
    private final SellerRepository sellerRepository;



    public void insertCoupon(CouponDTO couponDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }
        String sellerId = ((Seller) authentication.getPrincipal()).getUser().getUid();
        log.info("sellerId---------------" + sellerId);
        Coupon coupon = Coupon.builder()
                .couponName(couponDTO.getCouponName())
                .couponType(couponDTO.getCouponType())
                .benefit(couponDTO.getBenefit())
                .startDate(couponDTO.getStartDate())
                .endDate(couponDTO.getEndDate())
                .Notes(couponDTO.getNotes())
                .rdate(LocalDate.now())
                .build();
        log.info("coupon------------------------" + coupon);
        Seller seller = sellerRepository.findByUser_Uid(sellerId)
                .orElseThrow(() -> new RuntimeException("seller not found"));

        coupon.setSeller(seller);

        couponRepository.save(coupon);
    }
}
