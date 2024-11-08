package com.lotteon.service.user;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Point;
import com.lotteon.entity.order.Order;
import com.lotteon.repository.order.OrderRepository;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.repository.user.PointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointService {


    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;


    @Transactional
    public Point createPoint(Long memberId, int amount, String description) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Point point = new Point();
        point.setAmount(amount);
        point.setRemainingPoints(amount); // 초기 잔여 포인트 설정
        point.setDescription(description);
        point.setMember(member); // 포인트와 멤버 연결
        Point savedPoint = pointRepository.save(point);
        log.info("Saved point: {}", savedPoint);

        return savedPoint;
    }

    @Transactional
    public void savePoint(Member member, double earnedPoints) {
        // Point 엔티티 생성
        Point point = new Point();
        point.setMember(member);
        point.setAmount(earnedPoints);
        point.setDescription("구매 확정 포인트 적립");

        // 포인트 기록 저장
        Point savedpoint = pointRepository.save(point);

        // Member의 총 포인트 업데이트
//        member.setPoints(savedpoint);
            member.savePoint(earnedPoints);

            memberRepository.save(member);
    }

}
