package com.lotteon.entity.cart;
/*

 ===
 추가사항 2024.10.26 하진희 price 관련 부분 long으로 변경
    이름 : 최영진
    날짜 : 2024-10-29
    @JsonInclude(JsonInclude.Include.NON_NULL) 추가


 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lotteon.dto.product.OptionItemDTO;
import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.OptionGroup;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ProductOptionCombination;
import com.mongodb.lang.Nullable;
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
    private long cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartId", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    @Nullable// or `@Nullable` annotation if using validation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="option_id")
    private Option option;


    @Nullable// or `@Nullable` annotation if using validation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="combinationId")
    private ProductOptionCombination productOptionCombination;

    private long optionGroupId;


    private String optionName;
    private String productName;
    private long price;
    private int quantity;
    private long totalPrice;
    private int points;
    private int discount;
    private long deliveryFee;
    private String imageUrl;
    private long calcShippingCost;



    public void totalPrice(){
        long shippingTerms = product.getShippingTerms();
        this.calcShippingCost = product.getShippingFee();


        if(optionGroupId != 0){
            long additional = productOptionCombination.getAdditionalPrice();
            this.price = this.price + additional;
            long discountAmount = ((this.price*discount/100)/10)*10;  //10원단위 절삭
            this.totalPrice = (this.price + discountAmount)*quantity;
        }else{
            long discountAmount = ((price * discount/ 100)/10)*10 ;
            this.totalPrice = (price - discountAmount) * quantity;
        }

        if(shippingTerms < totalPrice){
            this.totalPrice = this.price + product.getShippingFee();
            this.calcShippingCost=0;
        }


    }
}
