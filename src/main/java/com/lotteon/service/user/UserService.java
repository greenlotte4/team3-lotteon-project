package com.lotteon.service.user;

import com.lotteon.dto.User.MemberDTO;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.User.User;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.repository.user.SellerRepository;
import com.lotteon.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper getModelMapper;
    private final PointService pointService;

    public Optional<User> findUserByUid(String uid) {

        return userRepository.findByUid(uid); // 아이디로 사용자 검색
    }

    public Optional<List<User>> findAllUsers() {
        List<User> users = userRepository.findAll();
        log.info("Found " + users.size() + " users");
        return users.isEmpty() ? Optional.empty() : Optional.of(users);
    }

    public boolean login(String uid, String password) {
        Optional<User> optionalUser = findUserByUid(uid); // 아이디로 사용자 검색

        if (optionalUser.isEmpty()) {
            return false; // 사용자 없음
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPass())) {
            return false; // 비밀번호 틀림
        }
        if (user.getRole().equals(User.Role.MEMBER) || user.getRole().equals(User.Role.ADMIN) || user.getRole().equals(User.Role.SELLER)) {
            return true;
        } else {
            return false;
        }

    }

    //sellerLogin
    public boolean sellerlogin(String uid, String password) {
        Optional<User> optionalUser = findUserByUid(uid); // 아이디로 사용자 검색

        if (optionalUser.isEmpty()) {
            return false; // 사용자 없음
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(password, user.getPass())) {
            return false; // 비밀번호 틀림
        }
        if (user.getRole().equals(User.Role.ADMIN) || user.getRole().equals(User.Role.SELLER)) {
            return true;
        } else {
            return false;
        }

    }


    public void registerMember(User user, Member member) {
        user.setRole(User.Role.MEMBER); // 역할 설정
        userRepository.save(user); // User 저장

        member.setUser(user); // Member에 User 연결
        memberRepository.save(member); // Member 저장

        int congratulatoryPoints = 1000; // 지급할 포인트 수량
        pointService.createPoint(member.getId(), congratulatoryPoints, "회원가입 축하 포인트"); // 포인트 지급

    }

    public void registerSeller(User user, Seller seller) {
        user.setRole(User.Role.SELLER); // 역할 설정
        userRepository.save(user); // User 저장
        seller.setUser(user); // Seller에 User 연결
        sellerRepository.save(seller); // Seller 저장
    }

    public String getMemberNameByUsername(String username) {
        log.info("username passed to getMemberNameByUsername: " + username); // 확인용 로그
        Optional<Member> memberOptional = memberRepository.findByUser_Uid(username);

        return memberOptional.map(Member::getName).orElse("Unknown User");
    }

    public MemberDTO getByUsername(String username) {
        log.info("username passed to getByUsername: " + username); // 확인용 로그
        Optional<Member> memberOptional = memberRepository.findByUser_Uid(username);
        if(memberOptional.isPresent()) {
            Member member = memberOptional.get();
            MemberDTO memberDTO = getModelMapper.map(member, MemberDTO.class);
            return memberDTO;
        }
        return null;
    }

    public boolean checkUserId(String uid) {
        return userRepository.existsByUid(uid); // uid로 존재 여부 확인
    }

    // 이메일 중복 체크
    public boolean checkEmail(String email) {
        return memberRepository.existsByEmail(email); // email로 존재 여부 확인
    }

    // 휴대폰 중복 체크
    public boolean checkPhone(String hp) {
        return memberRepository.existsByHp(hp); // hp로 존재 여부 확인
    }

    public Optional<User> findById(String uid) {
        return userRepository.findById(uid); //uid를 사용하여 User조회
    }
}
