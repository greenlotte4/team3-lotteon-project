package com.lotteon.repository.cart;

import com.lotteon.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {
    Optional<Cart> findByUid(int uid);

    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems WHERE c.uid = :uid")
    Optional<Cart> findByCartUid(@Param("uid") int uid);

}
