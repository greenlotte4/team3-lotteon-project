package com.lotteon.service.user;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Point;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.repository.user.PointRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointService {


    private final PointRepository pointRepository;

    private final MemberRepository memberRepository;


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
}
