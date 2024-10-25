package com.lotteon.repository.Impl;

import com.lotteon.dto.product.*;
import com.lotteon.entity.User.QSeller;
import com.lotteon.entity.User.QUser;
import com.lotteon.entity.product.*;
import com.lotteon.repository.custom.ProductRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;


@RequiredArgsConstructor
@Log4j2
@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private QProduct qProduct = QProduct.product;
    private QOption qOption = QOption.option;
    private QProductDetails qProductDetails = QProductDetails.productDetails;
    private QUser qUser = QUser.user;
    private QProductFile qProductFile = QProductFile.productFile;
    private QSeller qSeller = QSeller.seller;


    //admin product list에서 사용
    @Override
    public Page<ProductListDTO> selectProductBySellerIdForList(String sellerId, PageRequestDTO pageRequest, Pageable pageable) {

        List<ProductListDTO> products = queryFactory
                .select(Projections.constructor(ProductListDTO.class,
                        qProduct.productId,
                        qProduct.categoryId,
                        qProduct.productName,
                        qProduct.price,
                        qProduct.stock,
                        qProduct.discount,
                        qProduct.shippingFee,
                        qProduct.shippingTerms,
                        qProduct.rdate,  // LocalDateTime 그대로 받기
                        qProduct.ProductDesc,
                        qProduct.sellerId,  // 이 부분 추가
                        qProduct.productCode,  // 이 부분 추가
                        qProduct.hit,
                        Projections.list(Projections.constructor(ProductFileDTO.class,
                                qProductFile.p_fno,
                                qProductFile.sName,
                                qProductFile.type
                        ))
                ))
                .from(qProduct)
                .leftJoin(qProduct.files, qProductFile)
                .where(qProduct.sellerId.eq(sellerId).and(qProductFile.type.eq("190")))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(qProduct.count())
                .from(qProduct)
                .where(qProduct.sellerId.eq(sellerId))
                .fetchOne().intValue();


        return new PageImpl<>(products, pageable,total);
    }

    @Override
    public Page<Tuple> selectProductForList(PageRequestDTO pageRequest, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ProductListDTO> selectProductForListByCategory(PageRequestDTO pageRequest, Pageable pageable) {
        List<ProductListDTO> products = queryFactory
                .select(Projections.constructor(ProductListDTO.class,
                        qProduct.productId,
                        qProduct.categoryId,
                        qProduct.productName,
                        qProduct.price,
                        qProduct.stock,
                        qProduct.discount,
                        qProduct.shippingFee,
                        qProduct.shippingTerms,
                        qProduct.rdate,  // LocalDateTime 그대로 받기
                        qProduct.ProductDesc,
                        qProduct.sellerId,  // 이 부분 추가
                        qProduct.productCode,  // 이 부분 추가
                        qProduct.hit,
                        Projections.list(Projections.constructor(ProductFileDTO.class,
                                qProductFile.p_fno,
                                qProductFile.sName,
                                qProductFile.type
                        ))
                ))
                .from(qProduct)
                .leftJoin(qProduct.files, qProductFile)
                .where(qProduct.categoryId.eq(pageRequest.getCategoryId()).and(qProductFile.type.eq("230")))
                .distinct()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.info("productgetO00000"+products);
        long total = queryFactory
                .select(qProduct.count())
                .from(qProduct)
                .where(qProduct.categoryId.eq(pageRequest.getCategoryId()))
                .fetchOne();


        return  new PageImpl<>(products, pageable, total);
    }
}
