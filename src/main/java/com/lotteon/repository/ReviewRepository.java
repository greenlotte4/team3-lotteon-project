package com.lotteon.repository;

import com.lotteon.entity.product.Review;
import com.lotteon.repository.custom.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> , ReviewRepositoryCustom {

    List<Review> findTop3ByOrderByRdateDesc();
}
