package com.lotteon.service.product;

import com.lotteon.config.RedirectToLoginException;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RedirectToLoginException("사용자가 인증되지 않았습니다.");
        }

        String username = authentication.getName();

        try {
            return userRepository.findByUid(username)
                    .orElseThrow(() -> new UsernameNotFoundException("유저가 없습니다."));
        } catch (UsernameNotFoundException e) {
            // 사용자 정보를 찾지 못한 경우 로그인 페이지로 리다이렉트
            throw new RedirectToLoginException("사용자가 인증되지 않았습니다.");
        }
    }
    public CartItem insertCartItem(CartRequestDTO cartRequestDTO) {

        if (cartRequestDTO.getQuantity() <= 0) {
            throw new RuntimeException("수량은 1 이상이어야 합니다,.");
        }

        User user = getUser();

        // 사용자의 장바구니를 가져오고, 없으면 새로 생성
        Cart cart = cartRepository.findByUserWithItems(user)
                .orElseGet(() -> {
                    log.info("카트가 없어서 새로 생성합니다.");
                    return createCart(user);
                });

        log.info("현재 카트 ID: {}", cart.getCartId());

        Product product = productRepository.findById(cartRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("상품이 없습니다."));

        Option option = optionRepository.findById(cartRequestDTO.getOptionId())
                .orElseThrow(() -> {
                    log.error("옵션 ID가 유효하지 않습니다: {}", cartRequestDTO.getOptionId());
                    return new RuntimeException("옵션이 없습니다.");
                });
        Optional<CartItem> existingItem = cartItemRepository
                .findByCart_CartIdAndProduct_ProductIdAndOption_Id
                        (cart.getCartId(), product.getProductId(), cartRequestDTO.getOptionId());

        CartItem cartItem = null;

        if (existingItem.isPresent()) {
            // 아이템이 이미 존재하는 경우 수량 업데이트
            cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + cartRequestDTO.getQuantity();
            long newTotalPrice = cartItem.getTotalPrice() + (cartRequestDTO.getFinalPrice() * cartRequestDTO.getQuantity());

            cartItem.setQuantity(newQuantity); // 수량 업데이트
            cartItem.setTotalPrice(newTotalPrice); // 총 가격 업데이트
            cartItemRepository.save(cartItem);

            log.info("기존 아이템 업데이트: {}, 새로운 수량: {}", cartItem.getProduct().getProductName(), newQuantity);

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
                    .optionName(option.getOptionName())
                    .option(option)
                    .build();

            cartItemRepository.save(newCartItem);
            cart.getCartItems().add(newCartItem); // 카트에 새 아이템 추가

            log.info("새로운 아이템 추가: {}, 수량: {}", newCartItem.getProductName(), newCartItem.getQuantity());

        }

        // 카트의 총 수량과 총 가격 업데이트
        updateCartSummary(cart);

        return cartItem; // 카트 아이템 반환

    }


    private void updateCartSummary(Cart cart) {

        int totalItemCount = (int) cart.getCartItems().stream()
                .map(item -> item.getOption().getId())
                .distinct()
                .count();

        cart.setItemQuantity(totalItemCount);
        cartRepository.save(cart); // 카트 저장
        log.info("업데이트된 카트 아이템 수량: {}", cart.getTotalPrice());
        log.info("업데이트된 카트 아이템 수량: {}", totalItemCount);

    }


    // 새로운 장바구니를 생성
    private Cart createCart(User user) {
        Cart newCart = Cart.builder()
                .user(user)
                .cartItems(new ArrayList<>())
                .rdate(LocalDateTime.now())
                .build();

        log.info("cart --------------" + newCart);
        return cartRepository.save(newCart);
    }

    //유저 검색후
    public List<CartItem> selectCartAll(){

        User user = getUser();
        log.info("user uid`````````------------------" + user.getUid());

        Cart cart = cartRepository.findByUser_Uid(user.getUid()).orElse(null);

        if (cart == null) {
            log.info("카트가 없습니다.");
            return Collections.emptyList(); // 빈 리스트 반환
        }

        List<CartItem> cartItems = cart.getCartItems();
        log.info("cartItems count!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!: " + cartItems.size());
        return cartItems;
    }

    public CartSummary calculateSelectedCartSummary(List<CartItem> selectedItems) {
        int totalQuantity = selectedItems.stream().mapToInt(CartItem::getQuantity).sum();
        long totalPrice = selectedItems.stream()
                .mapToLong(item -> (long) item.getPrice() * item.getQuantity()).sum();
        long totalDiscount = selectedItems.stream()
                .mapToLong(CartItem::getDiscount).sum();
        long totalDeliveryFee = selectedItems.stream()
                .mapToLong(CartItem::getDeliveryFee).sum();
        long totalOrderPrice = totalPrice - totalDiscount + totalDeliveryFee; // 배송비 더하기
        long totalPoints = selectedItems.stream().mapToLong(CartItem::getPoints).sum();

        log.info("totalQuantity :"+totalQuantity);
        log.info("totalPrice :"+totalPrice);
        log.info("totalDiscount :"+totalDiscount);
        log.info("totalDeliveryFee :"+totalDeliveryFee);
        log.info("totalOrderPrice :"+totalOrderPrice);
        log.info("totalPoints :"+totalPoints);
        return CartSummary.builder()
                .finalTotalDeliveryFee(totalDeliveryFee)
                .finalTotalPrice(totalPrice)
                .finalTotalQuantity(totalQuantity)
                .finalTotalDiscount(totalDiscount)
                .finalTotalOrderPrice(totalOrderPrice)
                .finalTotalPoints(totalPoints)
                .build();
    }


    @Transactional
    public void updateQuantity(Long cartItemId, int quantity) {

        log.info("일단 여기까진 들어옴");
        if (quantity < 1) {
            throw new RuntimeException("수량은 1 이상이어야 합니다.");
        }
        log.info("Received cartItemId: {}, quantity: {}", cartItemId, quantity);

        log.info("1111111111111111:"+cartItemId);
        log.info("2222222222222222:"+quantity);

        try {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new RuntimeException("장바구니에 해당 상품이 없습니다."));
            log.info("카트 아이템이다"+cartItemId);

            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            log.info("상품 {}의 수량이 {}로 업데이트되었습니다.", cartItemId, quantity);
        } catch (RuntimeException e) {
            log.error("오류 발생: {}", e.getMessage());
            throw e; // 예외를 다시 던져서 컨트롤러에서 처리
        }

    }

    public void deleteCartItem(List<Long> cartItemIds) {
        Long cartId = null; // 카트 ID를 저장할 변수
        int deletedItemCount = cartItemIds.size(); // 삭제할 아이템 수

        // 카트의 아이템 수 확인 (초기 아이템 수)
        if (!cartItemIds.isEmpty()) {
            CartItem firstItem = cartItemRepository.findById(cartItemIds.get(0))
                    .orElseThrow(() -> new RuntimeException("장바구니에 해당 상품이 읍다"));
            cartId = firstItem.getCart().getCartId();
        }

        for (Long cartItemId : cartItemIds) {
            // 카트 아이템을 먼저 조회하여 카트 ID를 저장
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new RuntimeException("장바구니에 해당 상품이 읍다"));
            // 카트 ID 저장
            if (cartId == null) {
                cartId = cartItem.getCart().getCartId();
            }

            cartItemRepository.delete(cartItem);
            cartItemRepository.flush();

            log.info("삭제 완료했다: cartItemId = " + cartItemId);

        }
        // 카트에 남은 아이템 수 확인
        long itemCount = cartItemRepository.countByCart_CartId(cartId);
        log.info("남은 아이템수!!!!!!!!!"+itemCount);
        log.info("삭제한 아이템 수: " + deletedItemCount + ", 삭제된 아이템 IDs: " + cartItemIds);

        if (itemCount == 0) {
           cartRepository.deleteById(cartId);
            log.info("카트 삭제 완료: cartId = " + cartId);
        } else {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("상품 아이디가 읍다"));

            updateCartSummary(cart);

        }
    }






}
