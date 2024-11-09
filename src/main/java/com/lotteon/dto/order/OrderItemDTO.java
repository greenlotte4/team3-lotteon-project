package com.lotteon.dto.order;

import com.lotteon.dto.product.OptionDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.order.OrderItem;
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
    private String customerId;  //  구매자
    private String customerName; //구매자이름


    private String productName;
    //selectOption
    private OptionDTO selectOption;


    public OrderItemDTO(OrderItem item) {
        this.orderItemId = item.getOrderItemId();
        this.savedPrice = item.getSavedPrice();
        this.orderPrice = item.getOrderPrice();
        this.savedDiscount = item.getSavedDiscount();
        this.point = item.getPoint();
        this.sellerUid = item.getSellerUid();
        this.optionDesc = item.getOptionDesc();
        this.optionId = item.getOptionId();
        this.combination = item.getCombination();
        this.combinationId = item.getCombinationId();
        this.stock = item.getStock();
        this.price = item.getPrice();
        this.traceNumber = item.getTraceNumber();
        this.shippingFees = item.getShippingFees();
        this.status = item.getStatus();

        // Product 엔티티의 정보
        if (item.getProduct() != null) {
            this.productId = item.getProduct().getProductId();  // Product ID 가져오기
            this.productName = item.getProduct().getProductName();  // Product 이름 가져오기
        }
    }
}
