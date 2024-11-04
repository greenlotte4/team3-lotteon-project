package com.lotteon.repository.product;
/*
    최영진:      Optional<Product> findById(long productId);추가

*/
import com.lotteon.entity.product.Product;
import com.lotteon.repository.custom.ProductRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> , ProductRepositoryCustom {

    Page<Product> findBySellerId(String sellerId, Pageable pageable);

    List<Product> findAllBySellerId(String sellerId); // 페이지 없이 모든 상품 반환

    Product findByProductId(Long productId);

    Optional<Product> findById(Long productId);
}
