package com.lotteon.repository.user;

import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUser_Uid(String uid);

    boolean existsByEmail(String email);

    boolean existsByHp(String hp);
}
