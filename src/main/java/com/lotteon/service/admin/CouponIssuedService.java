package com.lotteon.service.admin;

import com.lotteon.config.RedirectToLoginException;
import com.lotteon.dto.admin.CouponIssuedDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.repository.admin.CouponIssuedRepository;
import com.lotteon.repository.admin.CouponRepository;
import com.lotteon.repository.user.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponIssuedService {

    private final MemberRepository memberRepository;
    private final CouponIssuedRepository couponIssuedRepository;
    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;

    public String rendomIssuedId(){
        String issuedId;
        do{
            issuedId = UUID.randomUUID().toString().replaceAll("-","").substring(0,10);
        }while (couponIssuedRepository.existsById(issuedId));
        return issuedId;
    }


    public void insertCouponIssued(Member member, Coupon coupon) {
        CouponIssued couponIssued = CouponIssued.builder()
                .IssuanceNumber(rendomIssuedId())
                .couponId(coupon.getCouponId())
                .couponName(coupon.getCouponName())
                .couponType(coupon.getCouponType())
                .member(member) // member 객체를 직접 설정
                .build();

        couponIssuedRepository.save(couponIssued); // 엔티티 저장
    }




    public void selectCouponIssued(String couponIssuedId){}




    public void endCouponIssued(String couponIssuedId){}


}
