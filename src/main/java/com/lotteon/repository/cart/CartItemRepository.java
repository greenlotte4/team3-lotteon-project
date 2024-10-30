package com.lotteon.repository.cart;

import com.lotteon.entity.cart.Cart;
import com.lotteon.entity.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/*
    날짜 : 2024-10-30
    최영진 : int countByCartId(Long cartId);추가 List<CartItem> findByCart(Cart cart); 추가

 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {


    Optional<CartItem> findByCart_CartIdAndProduct_ProductIdAndOption_Id(Long cartId, Long productId, long optionId);

    long countByCart_CartId(long cartId);
    List<CartItem> findByCart(Cart cart);

}
