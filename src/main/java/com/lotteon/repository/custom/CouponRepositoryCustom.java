package com.lotteon.repository.custom;

import com.lotteon.dto.admin.CouponPageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponRepositoryCustom {

   Page<CouponPageDTO> selectCouponByUserIdForList (Long  sellerId , Pageable pageable);

   Page<CouponPageDTO> searchCoupons(Long  sellerId, Pageable pageable, String searchType, String searchValue); // 검색 기능 추가

   Page<CouponPageDTO> selectAllCouponsForAdmin(Pageable pageable);

}
