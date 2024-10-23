package com.lotteon.repository.user;

import com.lotteon.entity.User.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

    Optional<Seller> findById(long id);
    Optional<Seller> findByUserUid (String uid);


}

