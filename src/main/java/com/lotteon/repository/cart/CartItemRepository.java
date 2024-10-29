package com.lotteon.repository.cart;

import com.lotteon.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    Optional<CartItem> findByCart_CartIdAndProduct_ProductIdAndOption_Id(Long cartId, Long productId, long optionId);

}
