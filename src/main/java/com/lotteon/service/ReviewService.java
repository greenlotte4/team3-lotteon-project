package com.lotteon.service;

import com.lotteon.dto.admin.PageRequestDTO;
import com.lotteon.dto.admin.PageResponseDTO;
import com.lotteon.dto.product.ReviewDTO;
import com.lotteon.dto.product.ReviewRequestDTO;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.Review;
import com.lotteon.entity.product.ReviewFile;
import com.lotteon.repository.ReviewRepository;
import com.lotteon.repository.product.ProductRepository;
import groovy.util.logging.Log4j2;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final ModelMapper modelMapper;

    @PostConstruct
    public void init() {
//        modelMapper.addMappings(new PropertyMap<Review, ReviewDTO>() {
//            @Override
//            protected void configure() {
//                skip(destination.getPReviewFiles()); // pReviewFiles 필드를 무시
//                skip(destination.getSavedReviewFiles()); // savedReviewFiles도 무시
//                // 필요한 다른 매핑 설정 추가
//            }
//        });
    }

    public boolean saveReview(ReviewRequestDTO reviewDTO) {
        Long productId = reviewDTO.getProductId(); // ReviewDTO에서 productId 가져오기
        Product product = productRepository.findById(productId).orElse(null);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();


        if (product == null) {
            return false; // Product가 없을 경우 저장 중단
        }

        Review review = new Review();
        review.setProduct(product);
        review.setRating(reviewDTO.getRating()); // DTO에서 rating 가져오기
        review.setContent(reviewDTO.getContent()); // DTO에서 content 가져오기
        review.setWriter(currentUsername);
        review.setRdate(LocalDateTime.now());

        // 파일 업로드 로직 호출
        // fileService의 uploadReviewFiles 메서드 호출하여 파일 업로드
        ReviewRequestDTO uploadedReviewDTO = fileService.uploadReviewFiles(reviewDTO);

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

    public List<Review> getRecentReviews() {
        return reviewRepository.findTop3ByOrderByRdateDesc();
    }


    public List<Review> getAllReviews() {
        return reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "rdate")); // 최신 순으로 정렬
    }

    public PageResponseDTO<ReviewDTO> getAllReviewss(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Review> pageReview = reviewRepository.selectReviewAllForList(pageRequestDTO, pageable);

        List<ReviewDTO> reviewList = pageReview.stream()
                .map(review -> {
                    ReviewDTO reviewDTO = review.ToDTO(review); // DTO 변환
                    reviewDTO.setWriter(reviewDTO.getMaskedWriter()); // 마스킹된 아이디 설정
                    return reviewDTO; // 마스킹된 아이디가 포함된 DTO 반환
                })
                .collect(Collectors.toList());

        int total = (int) pageReview.getTotalElements();

        return PageResponseDTO.<ReviewDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(reviewList)
                .total(total)
                .build();
    }

    public PageResponseDTO<ReviewDTO> getAllReviewsss(PageRequestDTO pageRequestDTO, Long productId) {
        // `productId`를 `type` 필드로 설정
        pageRequestDTO.setType(String.valueOf(productId));

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Review> pageReview = reviewRepository.selectReviewAllForList(pageRequestDTO, pageable);

        // 리뷰 엔티티를 DTO로 변환 및 마스킹 처리
        List<ReviewDTO> reviewList = pageReview.stream()
                .map(review -> {
                    ReviewDTO reviewDTO = review.ToDTO(review); // DTO 변환
                    reviewDTO.setWriter(reviewDTO.getMaskedWriter()); // 마스킹된 아이디 설정
                    return reviewDTO;
                })
                .collect(Collectors.toList());

        int total = (int) pageReview.getTotalElements();

        return PageResponseDTO.<ReviewDTO>builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(reviewList)
                .total(total)
                .build();
    }

}
