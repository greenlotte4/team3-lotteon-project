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


    public void insertCouponIssued(Member member, Coupon coupon, Product product) {
        CouponIssued couponIssued = CouponIssued.builder()
                .issuanceNumber(rendomIssuedId())
                .couponId(coupon.getCouponId())
                .couponName(coupon.getCouponName())
                .couponType(coupon.getCouponType())
                .memberName(member.getName())
                .productName(product.getProductName())
                .member(member) // member 객체를 직접 설정
                .productId(coupon.getProduct().getProductId())
                .build();

        couponIssuedRepository.save(couponIssued); // 엔티티 저장
    }







    public void endCouponIssued(String couponIssuedId){}


}
