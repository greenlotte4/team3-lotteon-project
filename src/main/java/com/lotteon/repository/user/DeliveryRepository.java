package com.lotteon.repository.user;

import com.lotteon.entity.User.Delivery;
import com.lotteon.entity.User.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByMemberId(Long memberId); // 특정 회원의 배송지 목록 조회

//    void setDefaultAddressForMember(String uid, Member member);
}
