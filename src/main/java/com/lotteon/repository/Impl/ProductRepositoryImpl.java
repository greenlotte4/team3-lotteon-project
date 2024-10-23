package com.lotteon.repository.Impl;

import com.lotteon.dto.product.PageRequestDTO;
import com.lotteon.entity.User.QUser;
import com.lotteon.entity.product.*;
import com.lotteon.repository.custom.ProductRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


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

    @Override
    public Page<Product> selectProductBySellerIdForList(String sellerId, PageRequestDTO pageRequest,Pageable pageable) {

        List<Product> products = queryFactory
                .select(qProduct)
                .from(qProduct)
                .leftJoin(qProduct.files,qProductFile).fetchJoin()
                .leftJoin(qProduct.options,qOption).fetchJoin()
                .leftJoin(qProduct.productDetails,qProductDetails).fetchJoin()
                .where(qProduct.sellerId.eq(sellerId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(qProduct.count())
                .from(qProduct)
                .where(qProduct.sellerId.eq(sellerId))
                .fetchOne().intValue();


        return new PageImpl<>(products, pageable,total);
    }
}
