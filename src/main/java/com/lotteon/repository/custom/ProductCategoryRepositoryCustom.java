package com.lotteon.repository.custom;


import com.lotteon.entity.product.ProductCategory;

import java.util.List;

public interface ProductCategoryRepositoryCustom {


    public List<ProductCategory> selectProductCategoryByParentId(int parentId);
}
