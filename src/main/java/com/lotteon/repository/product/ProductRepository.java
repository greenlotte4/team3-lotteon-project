package com.lotteon.repository.product;

import com.lotteon.entity.product.Product;
import com.lotteon.repository.custom.ProductRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> , ProductRepositoryCustom {

    public List<Product> findBySellerId(String sellerId);
}
