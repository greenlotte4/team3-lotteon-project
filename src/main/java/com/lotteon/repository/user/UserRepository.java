package com.lotteon.repository.user;

import com.lotteon.dto.User.MemberDTO;
import com.lotteon.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUid(String uid);

    boolean existsByUid(String uid);

//    @Query("SELECT new com.lotteon.dto.User.MemberDTO(m.id, m.name, m.gender, m.email, m.hp, m.postcode, m.addr, m.addr2, m.point, m.grade, u.uid, m.regDate) " +
//            "FROM User u JOIN u.member m")
//    List<MemberDTO> findAllMemberDetails();




}
