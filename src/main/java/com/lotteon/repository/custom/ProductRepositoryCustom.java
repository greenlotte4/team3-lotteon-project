package com.lotteon.repository.custom;

import com.lotteon.dto.product.PageRequestDTO;
import com.lotteon.entity.product.Product;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;


public interface ProductRepositoryCustom {
    public Page<Product> selectProductBySellerIdForList(String sellerId, PageRequestDTO pageRequest,Pageable pageable );
}
