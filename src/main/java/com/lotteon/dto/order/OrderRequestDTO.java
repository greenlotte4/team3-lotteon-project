package com.lotteon.dto.order;

import com.lotteon.dto.product.request.BuyNowRequestDTO;
import com.lotteon.entity.User.Member;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {

  private List<BuyNowRequestDTO> productDataArray;

  private String uid;
  private Member member;  //주문자
  private String receiver;
  private String hp;
  private String postcode;
  private String addr1;
  private String addr2;
  private String discount; //할인율
  private long TotalDiscount;
  private long usedPointResult;
  private long usedCouponResult;
  private long totalShippingFee;
  private long totalFinalPrice;
  private String credit;
  private long couponId;
  private String shippingInfo;




}
