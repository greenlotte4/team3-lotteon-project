package com.lotteon.repository.product;


import com.lotteon.entity.product.ProductCategory;
import com.lotteon.repository.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>  {

    public List<ProductCategory> findByLevel(int level);
    public List<ProductCategory> findByParentId(Long parentId);

}
