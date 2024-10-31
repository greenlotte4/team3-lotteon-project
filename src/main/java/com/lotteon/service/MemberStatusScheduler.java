package com.lotteon.service;

import com.lotteon.entity.User.Member;
import com.lotteon.repository.user.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MemberStatusScheduler {

    @Autowired
    private MemberRepository memberRepository; // MemberRepository를 주입받습니다.

    @Scheduled(fixedRate = 60000) // 1분마다 실행
//    @Scheduled(fixedRate = 60000*60*24*30*3) // 3달 마다 실행
    public void updateDormantMembers() {
        // 현재 시간을 가져옵니다.
        LocalDateTime now = LocalDateTime.now();

        // 1분 이상 지난 회원들을 휴면 상태로 변경합니다.
        List<Member> members = memberRepository.findAll(); // 모든 회원 조회
        for (Member member : members) {
            if (member.getRegDate().plusMinutes(1).isBefore(now) && member.getStatus() == Member.MemberStatus.ACTIVE) {
                member.setStatus(Member.MemberStatus.DORMANT); // 상태 변경
                memberRepository.save(member); // 변경 사항 저장
            }
        }
    }
}
