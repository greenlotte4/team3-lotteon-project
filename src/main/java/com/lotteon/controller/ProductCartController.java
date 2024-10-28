package com.lotteon.controller;


import com.lotteon.dto.product.cart.CartRequestDTO;
import com.lotteon.entity.User.User;
import com.lotteon.entity.cart.Cart;
import com.lotteon.entity.cart.CartItem;
import com.lotteon.repository.cart.CartRepository;
import com.lotteon.service.product.MarketCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductCartController {



    private final MarketCartService marketCartService;
    private final CartRepository cartRepository;


    @GetMapping("/list")
    public ResponseEntity<List<CartItem>> selectCartAll(@AuthenticationPrincipal UserDetails userDetails) {

       if(userDetails == null) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
       }

       try {
           List<CartItem> cartItems = marketCartService.selectCartAll();
           return ResponseEntity.ok(cartItems);
       } catch (Exception e) {
           log.error(e.getMessage());
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
       }

    }


    @PostMapping("/cart")
    public ResponseEntity<List<CartItem>> insertCart(@RequestBody CartRequestDTO cartRequestDTO) {
        log.info("호출됨0000000000000000000000000000");
        try {
            User user = marketCartService.getUser();

            int stock = cartRequestDTO.getQuantity();
            log.info("stock ====================================== " + stock);
            log.info("Requested stock: {}-------------------", cartRequestDTO.getQuantity());

            // 장바구니에 상품 추가
            Cart cart = marketCartService.insertCartItem(cartRequestDTO.getProductId(), stock, cartRequestDTO.getOptionId());
            // 카트의 아이템 리스트를 응답으로 반환
            List<CartItem> cartItems = cart.getCartItems();
            return ResponseEntity.status(HttpStatus.CREATED).body(cartItems);
        } catch (Exception e) {
            log.error("카트 추가중 오류=========================" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
