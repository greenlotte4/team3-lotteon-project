package com.lotteon.repository.product;
/*
    최영진:      Optional<Product> findById(long productId);추가

*/
import com.lotteon.entity.product.Product;
import com.lotteon.repository.custom.ProductRepositoryCustom;
import groovy.lang.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> , ProductRepositoryCustom {

    public Page<Product> findBySellerId(String sellerId, Pageable pageable);

    public Optional<Product> findByProductId(Long productId);

    Optional<Product> findById(long productId);

//    @EntityGraph(attributePaths = {
//            "productDetails",
//            "optionGroups.optionItems",
//            "optionCombinations",
//            "files",
//            "reviews"
//    })
//    Optional<Product> findByProductId(Long productId);

//    //    int deleteAllByProductIdIn(List<Long> productIds);
//    @Query("SELECT p FROM Product p " +
//            "LEFT JOIN FETCH p.optionGroups og " +                 // Join optionGroups
//            "LEFT JOIN FETCH og.optionItems oi " +                 // Join optionItems within each optionGroup
//            "LEFT JOIN FETCH p.optionCombinations " +              // Join optionCombinations
//            "WHERE p.productId = :productId")
//    Product findProductWithOptions(@Param("productId") Long productId);

}
