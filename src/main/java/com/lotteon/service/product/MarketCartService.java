package com.lotteon.service.product;

import com.lotteon.entity.User.User;
import com.lotteon.entity.cart.Cart;
import com.lotteon.entity.cart.CartItem;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.cart.CartItemRepository;
import com.lotteon.repository.cart.CartRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class MarketCartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUid(username)
                .orElseThrow(() -> new RuntimeException("user not found"));// 사용자 ID 반환
    }

    public Cart insertCart(int productId, int stock, int price) {

        User user = getUser();
        // 사용자의 장바구니를 가져오고, 없으면 새로 생성
        Cart cart = cartRepository.findByUserWithItems(user).orElseGet(() -> createCart(user));

        // 제품 정보 조회
        Product product = productRepository.findById((long) productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 장바구니 아이템 생성 및 저장
        CartItem cartItem = CartItem.builder()
                .stock(stock)
                .price(product.getPrice())
                .cart(cart)
                .product(product)
                .build();

        cartItem.totalPrice();
        cartItemRepository.save(cartItem);
        log.info("Insert cart -------------- " + productId);
        log.info("cartItem ------------------" + cartItem);
        return cart;
    }

    // 새로운 장바구니를 생성
    private Cart createCart(User user) {
        Cart newCart = Cart.builder()
                .user(user)
                .rdate(LocalDateTime.now())
                .build();

        log.info("cart --------------" + newCart);
        return cartRepository.save(newCart);
    }

    public List<CartItem> selectCartAll(){

        User user = getUser();
        log.info("user uid`````````------------------"+user.getUid());

        Cart cart = cartRepository.findByUser_Uid(user.getUid())
                .orElseGet(() -> {
                    // 카트가 없으면 새 카트를 생성하고 저장
                    Cart newCart = createCart(user);
                    log.info("Created new cart for user: " + user.getUid());
                    return newCart;
                });
        List<CartItem> cartItems = cart.getCartItems();
        log.info("cartItems count: " + cartItems);
        return cartItems;
    }
}
