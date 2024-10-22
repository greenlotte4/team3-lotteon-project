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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;

    @ManyToOne
    @JoinColumn(name = "cartId", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false)
    private Product product;


    private int stock;
    private int price;
    private int discount;
    private int point;
    private int deliveryFee;
    private int totalPrice;

    public void totalPrice(){
        int discountAmount = (price * discount) / 100;
        this.totalPrice = (price - discountAmount) * stock + deliveryFee;
    }
}
