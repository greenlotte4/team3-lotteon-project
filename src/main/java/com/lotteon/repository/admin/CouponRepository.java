package com.lotteon.repository.admin;

import com.lotteon.entity.admin.Coupon;
import com.lotteon.repository.custom.CouponRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String>, CouponRepositoryCustom  {

    Page<Coupon> findBySellerId(Long sellerId, Pageable pageable);

}
