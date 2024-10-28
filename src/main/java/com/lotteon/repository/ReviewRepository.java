package com.lotteon.repository;

import com.lotteon.entity.product.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
