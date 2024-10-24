package com.lotteon.repository.admin;

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.entity.admin.Coupon;
import com.lotteon.repository.Impl.CouponRepositoryImpl;
import com.lotteon.repository.custom.CouponRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, String>, CouponRepositoryCustom  {

    Page<CouponDTO> findCoupons(String uid, Pageable pageable);
}
