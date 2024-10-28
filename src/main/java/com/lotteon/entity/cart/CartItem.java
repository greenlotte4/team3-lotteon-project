package com.lotteon.entity.cart;
/*

 ===
 추가사항 2024.10.26 하진희 price 관련 부분 long으로 변경
 */
import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    private int stock;
    private long price;
    private long discount;
    private int point;
    private int deliveryFee;
    private long totalPrice;



    public void totalPrice(){
        long discountAmount = (price * discount) / 100;
        this.totalPrice = (price - discountAmount) * stock + deliveryFee;
    }
}
