package com.lotteon.controller.user;

import com.lotteon.entity.User.Delivery;
import com.lotteon.service.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class DeliveryController {

    private final MemberService memberService;


    // 멤버별 배송지 목록 조회
    @GetMapping("/{memberId}/deliveries")
    public List<Delivery> getDeliveriesByMemberId(@PathVariable Long memberId) {
        return memberService.getDeliveryByMemberId(memberId);
    }

    // 새로운 배송지 추가
    @PostMapping("/{memberId}/delivery")
    public Delivery addDelivery(@PathVariable Long memberId, @RequestBody Delivery delivery) {
        return memberService.addDeliveryToMember(memberId, delivery);
    }


}
