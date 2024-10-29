package com.lotteon.service.user;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Seller;
import com.lotteon.repository.user.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    // 모든 회원 목록 조회
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // id로 특정 회원 조회
    public Optional<Member> getMemberByUid(String uid) {
        return memberRepository.findByUser_Uid(uid);
    }

    // uid로 특정회원 조회
    public Optional<Member> findByUserId(String uid) {
        return memberRepository.findByUser_Uid(uid);
    }

    // 회원 정보 수정
    @Transactional
    public Member updateMember(Long id, Member updatedMember) {
        Optional<Member> existingMemberOpt = memberRepository.findById(id);
        if (existingMemberOpt.isPresent()) {
            Member existingMember = existingMemberOpt.get();
            // 기존 회원 정보 업데이트
            existingMember.setName(updatedMember.getName());
            existingMember.setGender(updatedMember.getGender());
            existingMember.setEmail(updatedMember.getEmail());
            existingMember.setHp(updatedMember.getHp());
            existingMember.setPostcode(updatedMember.getPostcode());
            existingMember.setAddr(updatedMember.getAddr());
            existingMember.setAddr2(updatedMember.getAddr2());
            existingMember.setUserinfocol(updatedMember.getUserinfocol());
            // 기타 필드 업데이트...

            return memberRepository.save(existingMember);
        }
        return null; // 회원이 존재하지 않는 경우
    }

    // 회원 삭제
    @Transactional
    public void deleteMembersByIds(List<Long> memberIds) {
        for (Long memberId : memberIds) {
            Optional<Member> memberOptional = memberRepository.findById(memberId);
            if (memberOptional.isPresent()) {
                // Seller가 존재하면 삭제
                memberRepository.delete(memberOptional.get());
            } else {
                throw new EntityNotFoundException("일치하는 아이디의 seller가 없습니다.: " + memberId);
            }
        }
    }
}
