package com.lotteon.repository;

import com.lotteon.entity.ProdCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdCategoryRepository extends JpaRepository<ProdCategory,Long> {

    List<ProdCategory> findByParentIsNullOrderById();
    List<ProdCategory> findByParentOrderById(ProdCategory parent);;
}
