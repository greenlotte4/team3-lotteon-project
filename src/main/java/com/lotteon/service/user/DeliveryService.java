package com.lotteon.service.user;

import com.lotteon.dto.User.DeliveryDTO;
import com.lotteon.dto.User.MemberDTO;
import com.lotteon.entity.User.Delivery;
import com.lotteon.entity.User.Member;
import com.lotteon.repository.user.DeliveryRepository;
import com.lotteon.repository.user.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final MemberRepository memberRepository;

    public DeliveryService(DeliveryRepository deliveryRepository, MemberRepository memberRepository) {
        this.deliveryRepository = deliveryRepository;
        this.memberRepository = memberRepository;
    }

    // 배송지 등록
    public Delivery saveDelivery(Long memberId, Delivery delivery) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        delivery.setMember(member);
        return deliveryRepository.save(delivery);
    }


    public void saveDefaultDelivery(Member member){
        String postcode = member.getPostcode();
        String addr = member.getAddr();
        String addr2 = member.getAddr2();

        if (postcode != null && addr != null && addr2 != null) {
            // 배송지 정보를 사용하여 기본 배송지 설정 수행

            // 배송지 정보 설정
            Delivery defaultDelivery = new Delivery();
            defaultDelivery.setMember(member);
            defaultDelivery.setPostcode(member.getPostcode());
            defaultDelivery.setAddr(member.getAddr());
            defaultDelivery.setAddr2(member.getAddr2());
            defaultDelivery.setDefault(true); // 기본 배송지로 설정

            deliveryRepository.save(defaultDelivery);
            log.info("Default address set for user: " + member.getUid());
        } else {
            log.warn("Incomplete address information, default address not set.");
        }
    }

    // 특정 회원의 배송지 조회

    // memberId에 해당하는 모든 배송 정보를 DeliveryDTO 리스트로 반환
    public List<DeliveryDTO> getByMemberId(Long memberId) {
        // Delivery 엔티티에서 memberId로 모든 배송지 정보 조회
        List<Delivery> deliveries = deliveryRepository.findByMemberId(memberId);

        // 배송지 정보가 없다면 빈 리스트 반환
        if (deliveries.isEmpty()) {
            return Collections.emptyList();
        }

        // Delivery 엔티티 리스트를 DeliveryDTO 리스트로 변환
        return deliveries.stream()
                .map(delivery -> DeliveryDTO.builder()
                        .id(delivery.getId())
                        .name(delivery.getName())
                        .hp(delivery.getHp())
                        .postcode(delivery.getPostcode())
                        .addr(delivery.getAddr())
                        .addr2(delivery.getAddr2())
                        .isDefault(delivery.isDefault())
                        .deliveryMessage(delivery.getDeliveryMessage())
                        .entranceCode(delivery.getEntranceCode())
                        .build())
                .collect(Collectors.toList());
    }

    public boolean deleteDelivery(Long deliveryId) {
        Optional<Delivery> delivery = deliveryRepository.findById(deliveryId);
        if (delivery.isPresent()) {
            deliveryRepository.delete(delivery.get());
            return true;  // 삭제 성공
        } else {
            return false; // 배송지 없으면 삭제 실패
        }
    }

    // memberId와 deliveryId에 해당하는 배송 정보를 DeliveryDTO로 반환
    public DeliveryDTO getDeliveryById(Long memberId, Long deliveryId) {
        // Delivery 엔티티에서 memberId와 deliveryId로 특정 배송지 정보 조회
        Delivery delivery = (Delivery) deliveryRepository.findByIdAndMemberId(deliveryId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배송지 정보를 찾을 수 없습니다."));

        // Delivery 엔티티를 DeliveryDTO로 변환하여 반환
        return DeliveryDTO.builder()
                .id(delivery.getId())
                .name(delivery.getName())
                .hp(delivery.getHp())
                .postcode(delivery.getPostcode())
                .addr(delivery.getAddr())
                .addr2(delivery.getAddr2())
                .isDefault(delivery.isDefault())
                .deliveryMessage(delivery.getDeliveryMessage())
                .entranceCode(delivery.getEntranceCode())
                .build();
    }

//    public void setDefaultAddressForMember(String uid, Member member) {
//        String postcode = member.getPostcode();
//        String addr = member.getAddr();
//        String addr2 = member.getAddr2();
//
//        if (postcode != null && addr != null && addr2 != null) {
//            // 배송지 정보를 사용하여 기본 배송지 설정 수행
//
//            // 배송지 정보 설정
//            Delivery defaultDelivery = new Delivery();
//            defaultDelivery.setMember(member);
//            defaultDelivery.setPostcode(member.getPostcode());
//            defaultDelivery.setAddr(member.getAddr());
//            defaultDelivery.setAddr2(member.getAddr2());
//            defaultDelivery.setDefault(true); // 기본 배송지로 설정
//
//            deliveryRepository.setDefaultAddressForMember(uid, member);
//            log.info("Default address set for user: " + uid);
//        } else {
//            log.warn("Incomplete address information, default address not set.");
//        }
//    }
}