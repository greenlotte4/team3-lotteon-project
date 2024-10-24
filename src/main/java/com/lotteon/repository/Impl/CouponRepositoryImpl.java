package com.lotteon.repository.Impl;

import com.lotteon.dto.admin.CouponDTO;
import com.lotteon.entity.User.QSeller;
import com.lotteon.entity.admin.QCoupon;
import com.lotteon.repository.custom.CouponRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Log4j2
@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private QCoupon qCoupon = QCoupon.coupon;
    private QSeller qSeller = QSeller.seller;

    @Override
    public Page<CouponDTO> findCoupons(String uid, Pageable pageable) {
        JPAQuery<CouponDTO> query = queryFactory
                .select(Projections.constructor(CouponDTO.class,
                        qCoupon.couponId,
                        qCoupon.couponName,
                        qCoupon.status,
                        qCoupon.startDate,
                        qCoupon.endDate,
                        qCoupon.issuedCount,
                        qCoupon.usedCount))
                .from(qCoupon);

        // 관리자인 경우 모든 쿠폰 조회
        if (uid == null) {
            // 관리자는 조건 없이 모든 쿠폰을 조회
        } else {
            // 일반 사용자는 uid로 필터링
            ((JPAQuery<?>) query).leftJoin(qCoupon.seller, qSeller)
                    .where(qSeller.user.uid.eq(uid));
        }

        long total = query.fetchCount(); // 총 개수
        List<CouponDTO> coupons = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(coupons, pageable, total);
    }
}
