package com.lotteon.repository.custom;

import com.lotteon.dto.admin.CouponDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponRepositoryCustom {

   Page<CouponDTO> findCoupons (String uid , Pageable pageable);

}
