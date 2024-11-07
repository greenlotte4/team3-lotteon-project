package com.lotteon.dto.order;

import com.lotteon.dto.product.OptionItemDTO;
import com.lotteon.dto.product.cart.CartItemDTO;
import com.lotteon.dto.product.request.BuyNowRequestDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private OrderDTO order;
    private List<OrderItemDTO> orderItems;
    private long usePoint;
    private long couponResult;
    private long cartId;
    private List<Long> cartItems;


    @Builder
    public  OrderResponseDTO(OrderRequestDTO orderRequestDTO) {

        System.out.println("쿠폰 아이디!!!:"+orderRequestDTO.getCouponId());
        List<BuyNowRequestDTO> products = orderRequestDTO.getProductDataArray();

        this.usePoint=orderRequestDTO.getUsedPointResult();
        this.couponResult=orderRequestDTO.getUsedCouponResult();
        long totalFinalPrice =parseLongOrDefault(orderRequestDTO.getTotalFinalPrice(),0);
        System.out.println("여기 totalFinalPrice!!!!0"+totalFinalPrice);
        long totalShippingFee =parseLongOrDefault(orderRequestDTO.getTotalShippingFee(),0);
        long totalOriginalPrice = parseLongOrDefault(orderRequestDTO.getTotalOriginalPrice(),0);
        long productDiscount = parseLongOrDefault(orderRequestDTO.getProductDiscount(),0);
        System.out.println("여기!!!!productDiscount"+productDiscount);
        long totalDiscount = productDiscount+couponResult+usePoint;
        this.orderItems = new ArrayList<>();
        long point=0;
        long totaldiscount= usePoint + couponResult;
        long cartid = products.get(0).getCartId() == 0  ? 0 : products.get(0).getCartId();
        if(cartid>0){
            this.cartId = cartid;

        }
        List<Long> cartItemIds = new ArrayList<>();

        for (BuyNowRequestDTO buyNowRequestDTO : products) {

            long originalQuantity = buyNowRequestDTO.getQuantity();
            if(cartid>0){
                cartItemIds.add(buyNowRequestDTO.getCartItemId());
            }
            List<OptionItemDTO> optionItemDTOS = buyNowRequestDTO.getOptions();
            long additionalPrice=0;
            long combinationId=0;
            String OptionName=null;
            String combinationString=null;
            if(optionItemDTOS !=null){
               combinationId= optionItemDTOS.get(0).getCombinationId();
               additionalPrice = optionItemDTOS.get(0).getAdditionalPrice()==null?0:optionItemDTOS.get(0).getAdditionalPrice();
               OptionName = optionItemDTOS.get(0).getOptionName();
               combinationString = optionItemDTOS.get(0).getCombinationString();
            }

            long originalPrice = parseLongOrDefault(buyNowRequestDTO.getOriginalPrice(), 0)+additionalPrice;
            long finalPrice = Long.parseLong(buyNowRequestDTO.getFinalPrice()) + additionalPrice;
            if(orderRequestDTO.getCouponId()==0){
                point = finalPrice* (orderRequestDTO.getGradePercentage());
            }
            long discount= Long.parseLong(buyNowRequestDTO.getDiscount());
            long savedDiscount =(discount * originalPrice)*originalQuantity ;
            OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                    .price(originalPrice+additionalPrice)
                    .productId(parseLongOrDefault(buyNowRequestDTO.getProductId(), 0))
                    .combinationId(combinationId)
                    .optionDesc(OptionName)
                    .combination(combinationString)
                    .savedPrice(originalPrice)
                    .orderPrice(finalPrice)
                    .point(point)
                    .savedDiscount(savedDiscount)
                    .shippingFees(parseLongOrDefault(buyNowRequestDTO.getShippingFee(),0))
                    .shippingTerms(parseLongOrDefault(buyNowRequestDTO.getShippingTerms(),0))
                    .stock(originalQuantity)
                    .status(DeliveryStatus.PREPARING)
                    .build();

            this.orderItems.add(orderItemDTO);

        }

        this.order = OrderDTO.builder()
                .addr1(orderRequestDTO.getAddr1())
                .addr2(orderRequestDTO.getAddr2())
                .hp(orderRequestDTO.getHp())
                .memberHp(orderRequestDTO.getMemberHp())
                .memberName(orderRequestDTO.getMemberName())
                .couponId(orderRequestDTO.getCouponId())
                .isCoupon(orderRequestDTO.getCouponId() > 0)
                .expectedPoint(parseLongOrDefault(orderRequestDTO.getFinalOrderPoint(), 0))
                .postcode(orderRequestDTO.getPostcode())
                .receiver(orderRequestDTO.getReceiver())
                .shippingInfo(orderRequestDTO.getShippingInfo())
                .totalOriginalPrice(parseLongOrDefault(orderRequestDTO.getTotalOriginalPrice(), 0))
                .totalDiscount(totalDiscount)
                .productDiscount(productDiscount)
                .usedPoint(orderRequestDTO.getUsedPointResult())
                .totalQuantity(parseLongOrDefault(orderRequestDTO.getTotalOrderQuantity(), 0))
                .totalShipping(Long.parseLong(orderRequestDTO.getTotalShippingFee()))
                .couponDiscount(orderRequestDTO.getUsedCouponResult())
                .usedCoupon(orderRequestDTO.getUsedCouponResult())
                .pay(orderRequestDTO.getCredit())
                .postcode(orderRequestDTO.getPostcode())
                .totalOriginalPrice(totalOriginalPrice)
                .totalPrice(totalFinalPrice)
                .build();

        this.cartItems=cartItemIds;

    }

    private long parseLongOrDefault(String value, long defaultValue) {
        try {
            return value != null ? Long.parseLong(value.replaceAll(",", "")) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


}
