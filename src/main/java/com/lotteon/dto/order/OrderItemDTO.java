    package com.lotteon.dto.order;

    import com.lotteon.dto.User.SellerDTO;
    import com.lotteon.dto.product.OptionDTO;
    import com.lotteon.dto.product.ProductDTO;
    import com.lotteon.entity.User.Seller;
    import com.lotteon.entity.order.OrderItem;
    import com.lotteon.entity.product.Review;
    import jakarta.persistence.Transient;
    import lombok.*;

    import java.text.DecimalFormat;
    import java.util.ArrayList;
    import java.util.List;

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
        private long categoryId;
        private long savedPrice;
        private long savedDiscount;
        private long orderPrice;
        private List<Review> reviewContent;
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
        private String formattedPrice;



        public OrderItemDTO(OrderItem item, Seller seller, OrderDTO order) {
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
            DecimalFormat df = new DecimalFormat("###,###");
            this.formattedPrice = df.format(item.getOrderPrice());

            // Product 엔티티의 정보
            if (item.getProduct() != null) {
                this.productId = item.getProduct().getProductId();
                this.productName = item.getProduct().getProductName();
                this.categoryId = item.getProduct().getCategoryId();
                this.image = item.getProduct().getFile190(); // 이미지 URL 설정
                this.reviewContent = item.getProduct().getReviews();
            }

            // Seller와 관련된 정보
            if (seller != null) {

                this.seller = seller;  // Seller 객체 설정
                this.company = seller.getCompany();  // 회사명 가져오기
            }
            if (order != null) {
                this.order = order;  // OrderDTO 객체 설정
                this.orderId = order.getOrderId();  // orderId 설정
            }

        }

//        public OrderItemDTO(OrderItem item ,Seller seller, OrderDTO order) {
//            this.orderItemId = item.getOrderItemId();
//            this.savedPrice = item.getSavedPrice();
//            this.orderPrice = item.getOrderPrice();
//            this.savedDiscount = item.getSavedDiscount();
//            this.point = item.getPoint();
//            this.sellerUid = item.getSellerUid();
//            this.optionDesc = item.getOptionDesc();
//            this.optionId = item.getOptionId();
//            this.combination = item.getCombination();
//            this.combinationId = item.getCombinationId();
//            this.stock = item.getStock();
//            this.price = item.getPrice();
//            this.traceNumber = item.getTraceNumber();
//            this.shippingFees = item.getShippingFees();
//            this.status = item.getStatus();
//            DecimalFormat df = new DecimalFormat("###,###");
//            this.formattedPrice = df.format(item.getOrderPrice());
//
//            // Product 엔티티의 정보
//            if (item.getProduct() != null) {
//                this.productId = item.getProduct().getProductId();
//                this.productName = item.getProduct().getProductName();
//                this.categoryId = item.getProduct().getCategoryId();
//                this.image = item.getProduct().getFile190(); // 이미지 URL 설정
//            }
//
//            // Seller와 관련된 정보
//            if (seller != null) {
//                this.seller = seller;  // Seller 객체 설정
//                this.company = seller.getCompany();  // 회사명 가져오기
//            }
//            if (order != null) {
//                this.order = order;  // OrderDTO 객체 설정
//                this.orderId = order.getOrderId();  // orderId 설정
//            }
//        }
    }
