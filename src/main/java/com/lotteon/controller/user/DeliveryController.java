package com.lotteon.controller.user;

import com.lotteon.dto.User.DeliveryDTO;
import com.lotteon.entity.User.Delivery;
import com.lotteon.service.user.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/member/{memberId}")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    // 배송지 등록 엔드포인트
    @PostMapping("/delivery")
    public ResponseEntity<Delivery> registerDelivery(@PathVariable Long memberId, @RequestBody Delivery delivery) {

        System.out.println("registerDelivery: " +delivery.toString());
        try {
            Delivery savedDelivery = deliveryService.saveDelivery(memberId, delivery);
            return ResponseEntity.ok(savedDelivery);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body((Delivery) Collections.singletonMap("error", "배송지 등록 실패"));
        }
    }

    // 특정 회원의 배송지 목록 조회 엔드포인트
//    @GetMapping("/deliveries")
    public ResponseEntity<List<DeliveryDTO>> getDeliveries(@PathVariable Long memberId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<DeliveryDTO> deliveries = deliveryService.getByMemberId(memberId);
        return ResponseEntity.ok(deliveries);
    }
}
