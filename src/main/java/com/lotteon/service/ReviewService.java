package com.lotteon.service;

import com.lotteon.dto.product.ReviewDTO;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.Review;
import com.lotteon.entity.product.ReviewFile;
import com.lotteon.repository.ReviewRepository;
import com.lotteon.repository.product.ProductRepository;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;

    public boolean saveReview(ReviewDTO reviewDTO) {
        Long productId = reviewDTO.getProductId(); // ReviewDTO에서 productId 가져오기
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return false; // Product가 없을 경우 저장 중단
        }

        Review review = new Review();
        review.setProduct(product);
        review.setRating(reviewDTO.getRating()); // DTO에서 rating 가져오기
        review.setContent(reviewDTO.getContent()); // DTO에서 content 가져오기
        review.setRdate(LocalDateTime.now());

        // 파일 업로드 로직 호출
        reviewDTO.setPReviewFiles(reviewDTO.getPReviewFiles()); // DTO에 파일 리스트 설정

        // fileService의 uploadReviewFiles 메서드 호출하여 파일 업로드
        ReviewDTO uploadedReviewDTO = fileService.uploadReviewFiles(reviewDTO);

        // 업로드된 파일 정보를 Review 엔티티에 설정
        List<ReviewFile> reviewFiles = uploadedReviewDTO.getSavedReviewFiles();

        // 리뷰 파일을 Review 객체에 추가
        for (ReviewFile reviewFile : reviewFiles) {
            reviewFile.setReview(review); // Review와의 관계 설정
        }
        review.setPReviewFiles(reviewFiles); // 수정: reviewFiles에 올바르게 설정

        // DB에 Review 저장
        reviewRepository.save(review);

        return true;
    }
}
