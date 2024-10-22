package com.lotteon.controller;


import com.lotteon.entity.cart.CartItem;
import com.lotteon.service.product.MarketCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductCartController {

    private final MarketCartService cartService;

    @GetMapping("/cart")
    public ResponseEntity<List<CartItem>> selectCartAll(@AuthenticationPrincipal UserDetails userDetails) {
            List<CartItem> cartItems = cartService.selectCartAll();
            return ResponseEntity.ok(cartItems);


    }
}
