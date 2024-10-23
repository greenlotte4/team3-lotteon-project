package com.lotteon.repository.user;

import com.lotteon.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUid(String uid);

    boolean existsByUid(String uid);



}
