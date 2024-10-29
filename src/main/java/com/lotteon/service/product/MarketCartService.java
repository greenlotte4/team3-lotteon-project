package com.lotteon.service.product;

import com.lotteon.dto.product.cart.CartRequestDTO;
import com.lotteon.dto.product.cart.CartSummary;
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
import java.util.Optional;

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

    public CartItem insertCartItem(CartRequestDTO cartRequestDTO) {

        if (cartRequestDTO.getQuantity() <= 0) {
            throw new RuntimeException("수량은 1 이상이어야 합니다,.");
        }

        User user = getUser();
        // 사용자의 장바구니를 가져오고, 없으면 새로 생성
        Cart cart = cartRepository.findByUserWithItems(user)
                .orElseGet(() -> createCart(user));

        Product product = productRepository.findById(cartRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("상품이 없습니다."));

        Option option = optionRepository.findById(cartRequestDTO.getOptionId())
                .orElseThrow(() -> {
                    log.error("옵션 ID가 유효하지 않습니다: {}", cartRequestDTO.getOptionId());
                    return new RuntimeException("옵션이 없습니다.");
                });
        Optional<CartItem> existingItem = cartItemRepository
                .findByCart_CartIdAndProduct_ProductIdAndOption_Id(
        cart.getCartId(), product.getProductId(), cartRequestDTO.getOptionId());
        CartItem cartItem;
        if (existingItem.isPresent()) {
            // 아이템이 이미 존재하는 경우 수량 업데이트
            cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + cartRequestDTO.getQuantity();
            long newTotalPrice = cartItem.getTotalPrice() + (cartRequestDTO.getFinalPrice() * cartRequestDTO.getQuantity());

            cartItem.setQuantity(newQuantity); // 수량 업데이트
            cartItem.setTotalPrice(newTotalPrice); // 총 가격 업데이트
            cartItemRepository.save(cartItem);
            return cartItem; // 업데이트된 아이템 반환
        } else {
            // 새로운 아이템 생성 및 저장
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .productName(product.getProductName())
                    .imageUrl(product.getFile190())
                    .quantity(cartRequestDTO.getQuantity())
                    .points(product.getPoint())
                    .deliveryFee(product.getShippingFee())
                    .price(cartRequestDTO.getOriginalPrice())
                    .totalPrice((int) cartRequestDTO.getFinalPrice())
                    .discount((int) cartRequestDTO.getDiscount())
                    .deliveryFee(cartRequestDTO.getShippingFee())
                    .option(option)
                    .build();

            cartItemRepository.save(newCartItem);
            return newCartItem; // 새로 추가된 아이템 반환
        }

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

    //유저 검색후
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
        log.info("cartItems count!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!: " + cartItems.size());
        return cartItems;
    }

    public CartSummary calculateCartSummary(List<CartItem> cartItems) {

        int totalQuantity = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        double totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        double totalDiscount = cartItems.stream()
                .mapToDouble(CartItem::getDiscount).sum(); // 할인 총합
        double totalDeliveryFee = cartItems.stream()
                .mapToDouble(CartItem::getDeliveryFee).sum(); // 배송비 총합
        double totalOrderPrice = totalPrice - totalDiscount - totalDeliveryFee;
        double totalPoints = cartItems.stream().mapToDouble(CartItem::getPoints).sum();



        return  CartSummary.builder()
                .finalTotalDeliveryFee(totalDeliveryFee)
                .finalTotalPrice(totalPrice)
                .finalTotalQuantity(totalQuantity)
                .finalTotalDiscount(totalDiscount)
                .finalTotalOrderPrice(totalOrderPrice)
                .finalTotalPoints(totalPoints)
                .build();
    }
}
