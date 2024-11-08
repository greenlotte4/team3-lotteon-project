package com.lotteon.repository.order;

import com.lotteon.entity.order.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUidOrderByOrderDateDesc(String userId);


    public Order findByOrderId(Long orderId);

    public List<Order> findByUid(String uid, Pageable pageable);

    @Query("SELECT o, oi " +
            "FROM Order o " +
            "JOIN o.orderProducts oi " +
            "JOIN oi.product p " +
            "WHERE o.uid = :uid")
    List<Object[]> findOrderAndOrderItemsByUid(@Param("uid") String uid);


    @Query("SELECT o, oi.orderItemId, p.file190 " +
            "FROM Order o " +
            "JOIN o.orderProducts oi " +
            "JOIN oi.product p " +
            "WHERE o.uid = :uid")
    Page<Object[]> findOrderWithProductImages(@Param("uid") String uid, Pageable pageable);

}
