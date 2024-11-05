package com.lotteon.entity.order;


import com.lotteon.entity.product.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
@Entity
@Table(name= "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    private String uid;
    private long totalOriginalPrice;   // 원래가격
    private long totalPrice;   // 할인된 금액
    private long totalQuantity;  //총 수량
    @Builder.Default
    private long totalDiscount=0;
    private long totalShipping;
    @Builder.Default
    private long expectedPoint=0;
    @Builder.Default
    private long productDiscount=0;

    private String receiver;
    private String hp;
    private String postcode;
    private String addr1;
    private String addr2;
    private String shippingInfo;
    private long usedPoint;   //사용 포인트
    private long usedCoupon;   //사용 포인트
    private String memberName;
    private String memberHp;

    @CreationTimestamp
    private LocalDateTime orderDate;
    private String orderStatus;
    private String pay;
    //쿠폰 사용유무
    private boolean isCoupon;
    private long couponId;


    // OrderProduct와의 OneToMany 관계 설정
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderProducts;


}
