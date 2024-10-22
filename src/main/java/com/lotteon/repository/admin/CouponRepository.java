package com.lotteon.repository.admin;

import com.lotteon.entity.admin.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
}
