package com.lotteon.entity.cart;
/*

 ===
 추가사항 2024.10.26 하진희 price 관련 부분 long으로 변경
    이름 : 최영진
    날짜 : 2024-10-29
    @JsonInclude(JsonInclude.Include.NON_NULL) 추가


 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "cart")
@Table(name = "cartItem")
@JsonInclude(JsonInclude.Include.NON_NULL)

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
    @JoinColumn(name ="option_id")
    private Option option;

    private String optionName;

    private String productName;
    private int price;
    private int quantity;
    private long totalPrice;
    private int points;
    private int discount;
    private int deliveryFee;
    private String imageUrl;


    public void totalPrice(){
        long discountAmount = (price * discount) / 100;
        this.totalPrice = (price - discountAmount) * quantity + deliveryFee;
    }
}
