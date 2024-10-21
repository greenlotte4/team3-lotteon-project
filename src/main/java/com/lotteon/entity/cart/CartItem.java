package com.lotteon.entity.cart;

import com.lotteon.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "cartItem")
public class CartItem {

    @Id
    @GeneratedValue
    private int cartItemId;


    private int stock;
    private int price;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}
