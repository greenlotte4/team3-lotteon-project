package com.lotteon.repository.Impl;

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.repository.custom.CouponRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CouponRepositoryImpl implements CouponRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CouponDTO> findCoupons(String uid, String grade, Pageable pageable) {
        if("ADMIN".equalsIgnoreCase(grade)){
            // 어드민인 경우 모든 쿠폰 조회
             String countQueryStr = "SELECT COUNT(c) FROM Coupon c";
            Long totalCount = entityManager.createQuery(countQueryStr, Long.class).getSingleResult();

            String queryStr = "SELECT new com.lotteon.dto.admin.CouponDTO(c.id, c.couponName, c.status) FROM Coupon c";
            TypedQuery<CouponDTO> query = entityManager.createQuery(queryStr, CouponDTO.class)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());

            List<CouponDTO> coupons = query.getResultList();
            return new PageImpl<>(coupons, pageable, totalCount);
        } else {
            String countQueryStr = "SELECT COUNT(c) FROM Coupon c JOIN c.seller s WHERE s.user.userUid = :uid";
            long totalCount = entityManager.createQuery(countQueryStr, Long.class)
                    .setParameter("uid", uid)
                    .getSingleResult();

            String queryStr = "SELECT new com.lotteon.dto.admin.CouponDTO(c.id, c.couponName, c.status) FROM Coupon c JOIN c.seller s WHERE s.user.userUid = :uid";
            TypedQuery<CouponDTO> query = entityManager.createQuery(queryStr, CouponDTO.class)
                    .setParameter("uid", uid)
                    .setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize());

            List<CouponDTO> coupons = query.getResultList();
            return new PageImpl<>(coupons, pageable, totalCount);

        }
    }
}
