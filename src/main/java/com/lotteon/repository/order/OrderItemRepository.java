package com.lotteon.repository.order;

import com.lotteon.entity.order.Order;
import com.lotteon.entity.order.OrderItem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    long countBySellerUid(String sellerUid);

    Page<OrderItem> findByOrder_Uid(String uid, Pageable pageable);

    List<OrderItem> findByOrder_OrderId(Long orderId);
  
    @Query("SELECT SUM(o.orderPrice) FROM OrderItem o WHERE o.sellerUid = :sellerUid")
    Long findTotalOrderPriceBySellerUid(@Param("sellerUid") String sellerUid);


    // 모든 판매자의 총 판매 수량을 계산하는 메서드
    @Query("SELECT COUNT(o) FROM OrderItem o")
    Long findTotalOrderCountForAllSellers();

    // 모든 판매자의 총 판매 금액을 계산하는 메서드
    @Query("SELECT SUM(o.orderPrice) FROM OrderItem o")
    Long findTotalOrderPriceForAllSellers();

    // 특정 판매자의 날짜 범위 내 주문 건수
    @Query("SELECT COUNT(oi) FROM OrderItem oi JOIN oi.order o WHERE oi.sellerUid = :sellerUid AND o.orderDate BETWEEN :start AND :end")
    long countOrdersBySellerAndDateRange(@Param("sellerUid") String sellerUid, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 특정 판매자의 날짜 범위 내 총 판매 금액
    @Query("SELECT SUM(oi.orderPrice) FROM OrderItem oi JOIN oi.order o WHERE oi.sellerUid = :sellerUid AND o.orderDate BETWEEN :start AND :end")
    Long sumSalesAmountBySellerAndDateRange(@Param("sellerUid") String sellerUid, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 모든 판매자의 어제 또는 오늘의 주문 건수를 가져오는 메서드
    @Query("SELECT COUNT(o) FROM Order o WHERE o.orderDate BETWEEN :start AND :end")
    long countOrdersByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // 모든 판매자의 어제 또는 오늘의 주문 금액 합계를 가져오는 메서드
    @Query("SELECT SUM(o.totalPrice) FROM Order o WHERE o.orderDate BETWEEN :start AND :end")
    long sumSalesAmountByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}


