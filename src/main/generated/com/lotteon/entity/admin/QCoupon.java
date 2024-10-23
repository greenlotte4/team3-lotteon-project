package com.lotteon.entity.admin;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCoupon is a Querydsl query type for Coupon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoupon extends EntityPathBase<Coupon> {

    private static final long serialVersionUID = 1998350966L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCoupon coupon = new QCoupon("coupon");

    public final StringPath benefit = createString("benefit");

    public final NumberPath<Integer> couponId = createNumber("couponId", Integer.class);

    public final StringPath couponName = createString("couponName");

    public final StringPath couponType = createString("couponType");

    public final StringPath endDate = createString("endDate");

    public final StringPath Notes = createString("Notes");

    public final DatePath<java.time.LocalDate> rdate = createDate("rdate", java.time.LocalDate.class);

    public final com.lotteon.entity.User.QSeller seller;

    public final StringPath startDate = createString("startDate");

    public QCoupon(String variable) {
        this(Coupon.class, forVariable(variable), INITS);
    }

    public QCoupon(Path<? extends Coupon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCoupon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCoupon(PathMetadata metadata, PathInits inits) {
        this(Coupon.class, metadata, inits);
    }

    public QCoupon(Class<? extends Coupon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.seller = inits.isInitialized("seller") ? new com.lotteon.entity.User.QSeller(forProperty("seller"), inits.get("seller")) : null;
    }

}

