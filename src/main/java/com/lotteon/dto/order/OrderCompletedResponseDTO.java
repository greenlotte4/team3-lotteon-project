package com.lotteon.dto.order;

import com.lotteon.dto.User.SellerDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.entity.product.Product;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Log4j2
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedResponseDTO {

    OrderDTO order;
    List<SellerDTO> sellers;
    private long originalTotalPrice;
    private long totalDiscount;
    private long expectedPoint;
    private long totalShippingFee;
    private long finalPaymentAmount;



    @Builder
    public OrderCompletedResponseDTO(OrderDTO order, Set<SellerDTO> sellerDTOs) {
        List<OrderItemDTO> orderItems = order.getOrderItems() != null ? order.getOrderItems() : new ArrayList<>();
        this.order = order;
        this.sellers = new ArrayList<>();

        // Initialize totals
        this.originalTotalPrice = 0;
        this.totalDiscount = 0;
        this.totalShippingFee = 0;
        this.expectedPoint=0;

        for (SellerDTO seller : sellerDTOs) {
            log.info("Processing seller: " + seller.getUid()); // Log each seller

            long sellerTotalPrice = 0;
            long sellerTotalDiscount = 0;
            long sellerShippingFee = 0;
            List<OrderItemDTO> sellerOrderItems = new ArrayList<>();

            for (OrderItemDTO orderItem : orderItems) {

                ProductDTO product = orderItem.getProduct();
                long productTotalPrice = product.getPrice() * orderItem.getStock();
                long productDiscount = (product.getPrice() * product.getDiscount()) / 100;

                // Calculate original price and discount for all items
                this.originalTotalPrice += productTotalPrice;
                this.totalDiscount += productDiscount;
                this.expectedPoint += orderItem.getPoint();

                // Calculate seller-specific totals
                sellerTotalPrice += productTotalPrice;
                sellerTotalDiscount += productDiscount;

                // Check shipping conditions
                if (product.getShippingTerms() > sellerTotalPrice) {
                    sellerShippingFee = product.getShippingFee();
                }

                // Add item to seller's list if seller matches
                if (seller.getUid().equals(orderItem.getSellerUid())) {
                    log.info("Seller " + seller.getUid() + " - Total Price: " + sellerTotalPrice + ", Total Discount: " + sellerTotalDiscount + ", Shipping Fee: " + sellerShippingFee);
                    sellerOrderItems.add(orderItem);
                }
            }

            // Finalize shipping fee for each seller
            seller.setTotalShipping(sellerShippingFee);
            this.totalShippingFee += sellerShippingFee;
            seller.setTotalPrice(sellerTotalPrice);
            seller.setTotalDiscount(sellerTotalDiscount);
            seller.setOrderItems(sellerOrderItems);
            this.sellers.add(seller);
            log.info("Seller Order Items: " + seller.getOrderItems());
        }
        this.totalDiscount = order.getTotalDiscount();
        // Calculate final payment amount
        this.finalPaymentAmount = this.originalTotalPrice - this.totalDiscount + this.totalShippingFee;
        log.info("Original Total Price: " + originalTotalPrice);
        log.info("Total Discount: " + totalDiscount);
        log.info("Total Shipping Fee: " + totalShippingFee);
        log.info("Final Payment Amount: " + finalPaymentAmount);


    }



}
