package com.lotteon.service.product;

import com.lotteon.dto.product.cart.CartDTO;
import com.lotteon.dto.product.cart.CartItemDTO;
import com.lotteon.entity.cart.Cart;
import com.lotteon.entity.cart.CartItem;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.cart.CartItemRepository;
import com.lotteon.repository.cart.CartRepository;
import com.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MarketCartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;


    public Cart insertCart(int uid, int productId, int stock, int price) {
        Optional<Cart> optionalCart = cartRepository.findByUid(uid);
        Cart cart;

        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            cart = Cart.builder()
                    .uid(uid)
                    .rdate(LocalDateTime.now())
                    .build();
            log.info("cart --------------" + cart);
            cartRepository.save(cart);
        }

        Product product = productRepository.findById((long) productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem cartItem = CartItem.builder()
                .stock(stock)
                .price(price)
                .cart(cart)
                .product(product)
                .build();
        cartItemRepository.save(cartItem);
        log.info("Insert cart -------------- " + productId);
        log.info("cartItem ------------------" + cartItem);
        return cart;
    }

    public List<CartItem> selectCartAll(int uid){
        Optional<Cart> optCart = cartRepository.findByUid(uid);

        if (optCart.isPresent()) {
            return optCart.get().getCartItems();
        }else {
            throw new RuntimeException("cart not found" + uid);
        }
    }
}
