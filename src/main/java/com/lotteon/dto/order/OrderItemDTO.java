package com.lotteon.dto.order;

import com.lotteon.dto.product.OptionDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.User.Seller;
import jakarta.persistence.Transient;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class OrderItemDTO {

    private long orderItemId;
    private OrderDTO order;
    private ProductDTO product;
    private long productId;
    private long savedPrice;
    private long savedDiscount;
    private long orderPrice;
    private long orderId;
    private long optionId;
    private String optionDesc;
    private String combination;
    private long combinationId;
    private long stock;
    private long price;
    private String traceNumber;
    private String sellerUid;
    private long shippingTerms;
    private long shippingFees;
    private long point;
    private DeliveryStatus status;
    @Transient
    private Seller seller;

    private String company;
    private String image;
    private String path;


    //selectOption
    private OptionDTO selectOption;


}
