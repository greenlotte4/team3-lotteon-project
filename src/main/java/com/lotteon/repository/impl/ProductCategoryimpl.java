package com.lotteon.repository.impl;

import com.lotteon.entity.product.ProductCategory;
import com.lotteon.entity.product.QProductCategory;
import com.lotteon.repository.custom.ProductCategoryRepositoryCustom;
import com.lotteon.repository.product.ProductCategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.List;


@Log4j2
@AllArgsConstructor
@Repository
public class ProductCategoryimpl implements ProductCategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QProductCategory qProductCategory = QProductCategory.productCategory;



    @Override
    public List<ProductCategory> selectProductCategoryByParentId(int parentId) {

        List<ProductCategory> productCategories = queryFactory.select(qProductCategory)
                .from(qProductCategory)
                .where(qProductCategory.parent.isNotNull())
                .fetch();

        log.info(productCategories);

        return productCategories;
    }
}
