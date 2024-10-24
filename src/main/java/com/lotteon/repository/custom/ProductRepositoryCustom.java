package com.lotteon.repository.custom;

import com.lotteon.dto.product.PageRequestDTO;
import com.lotteon.dto.product.ProductDTO;
import com.lotteon.dto.product.ProductListDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductRepositoryCustom {
    //admin List
    public Page<ProductListDTO> selectProductBySellerIdForList(String sellerId, PageRequestDTO pageRequest,Pageable pageable );

    //main list
    public Page<Tuple> selectProductForList( PageRequestDTO pageRequest,Pageable pageable );
    public Page<ProductListDTO> selectProductForListByCategory(PageRequestDTO pageRequest, Pageable pageable );




}
