package com.lotteon.service.product;

import com.lotteon.dto.product.cart.CartRequestDTO;
import com.lotteon.entity.User.User;
import com.lotteon.entity.cart.Cart;
import com.lotteon.entity.cart.CartItem;
import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.cart.CartItemRepository;
import com.lotteon.repository.cart.CartRepository;
import com.lotteon.repository.product.OptionRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class MarketCartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;


    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return userRepository.findByUid(username)
                .orElseThrow(() -> new RuntimeException("user not found"));// 사용자 ID 반환
    }

    public Cart insertCartItem(long productId, int stock, int price) {
        log.info("일단 호출됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1" +
                " productId: " + productId +
                ", stock: " + stock +
                ", price: " + price);

        log.info("Attempting to find product with ID: " + productId);

        if (stock <= 0){
            throw new RuntimeException("수량은 1 이상이어야 합니다,.");
        }

        User user = getUser();
        // 사용자의 장바구니를 가져오고, 없으면 새로 생성
        Cart cart = cartRepository.findByUserWithItems(user).orElseGet(() -> createCart(user));

        // 제품 정보 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        log.info("11111111111111111111111111Product found: " + product); // 여기 추가
        Long optionId = 1L; // Long으로 선언
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new RuntimeException("Option not found"));
        log.info("일단 호출됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!2" +
                " productId: " + productId +
                ", stock: " + stock +
                ", price: " + price);
        // 장바구니 아이템 생성 및 저장
        CartItem cartItem = CartItem.builder()
                .stock(stock)
                .price(product.getPrice())
                .discount(product.getDiscount()) // 할인율 추가
                .deliveryFee(product.getShippingFee()) // 배송비 추가
                .option(option)
                .cart(cart)
                .product(product)
                .build();
        log.info("카트아이템!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+cartItem);
        cartItem.totalPrice();
        cartItemRepository.save(cartItem);
        log.info("카트넣는다! -------------- " + productId);
        log.info("카트아이템! ------------------" + cartItem);
        log.info("일단 호출됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!3" +
                " productId: " + productId +
                ", stock: " + stock +
                ", price: " + price);
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
