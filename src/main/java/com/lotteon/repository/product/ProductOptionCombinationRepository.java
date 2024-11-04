package com.lotteon.repository.product;

import com.lotteon.entity.product.ProductOptionCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionCombinationRepository extends JpaRepository<ProductOptionCombination, Integer> {
}
