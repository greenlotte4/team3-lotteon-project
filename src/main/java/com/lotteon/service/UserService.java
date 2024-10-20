package com.lotteon.service;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.User.User;
import com.lotteon.repository.user.MemberRepository;
import com.lotteon.repository.user.SellerRepository;
import com.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final SellerRepository sellerRepository;

    public void registerMember(User user, Member member) {
        user.setRole(User.Role.MEMBER); // 역할 설정
        userRepository.save(user); // User 저장
        member.setUser(user); // Member에 User 연결
        memberRepository.save(member); // Member 저장
    }

    public void registerSeller(User user, Seller seller) {
        user.setRole(User.Role.SELLER); // 역할 설정
        userRepository.save(user); // User 저장
        seller.setUser(user); // Seller에 User 연결
        sellerRepository.save(seller); // Seller 저장
    }
}
