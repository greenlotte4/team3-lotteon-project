package com.lotteon.repository.Impl;
/*
    날짜 : 2024.10.20
    이름 : 하진희
    내용 : product CRUD
    ==================================
    추가내용 
    2024.10.26 - product List 사용 메서드 변경
 */


import com.lotteon.dto.product.*;
import com.lotteon.entity.User.QSeller;
import com.lotteon.entity.User.QUser;
import com.lotteon.entity.product.*;
import com.lotteon.repository.custom.ProductRepositoryCustom;
import com.lotteon.repository.user.SellerRepository;
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
    private final SellerRepository sellerRepository;
    private QProduct qProduct = QProduct.product;
    private QOption qOption = QOption.option;
    private QProductDetails qProductDetails = QProductDetails.productDetails;
    private QUser qUser = QUser.user;
    private QProductFile qProductFile = QProductFile.productFile;
    private QSeller qSeller = QSeller.seller;


    //admin product list에서 사용
    @Override
    public Page<Product> selectProductBySellerIdForList(String sellerId, PageRequestDTO pageRequest, Pageable pageable) {

        List<Product> products = queryFactory.select(qProduct)
                .from(qProduct)
                .where(qProduct.sellerId.eq(sellerId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(qProduct.count())
                .from(qProduct)
                .where(qProduct.sellerId.eq(sellerId))
                .fetchOne().longValue();


        return new PageImpl<>(products, pageable,total);
    }

    @Override
    public Page<Tuple> selectProductForList(PageRequestDTO pageRequest, Pageable pageable) {
        return null;
    }


    //main 3차 카테고리 선택시
    @Override
    public Page<Tuple> selectProductByCategory(PageRequestDTO pageRequest, Pageable pageable) {

        List<Tuple> products = queryFactory.select(qProduct,qSeller)
                .from(qProduct)
                .leftJoin(qSeller)
                .on(qSeller.user.uid.eq(qProduct.sellerId))
                .where(qProduct.categoryId.eq(pageRequest.getCategoryId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        log.info("did=dosidjflskdjfls : "+products);
        long total = queryFactory.select(qProduct.count())
                .from(qProduct)
                .where(qProduct.categoryId.eq(pageRequest.getCategoryId()))
                .fetchOne().longValue();

        log.info("totalllllllllllll:"+total);

        return new PageImpl<>(products, pageable,total);

    }


}
