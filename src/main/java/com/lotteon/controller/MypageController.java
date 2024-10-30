package com.lotteon.controller;

import com.lotteon.dto.admin.PageRequestDTO;
import com.lotteon.dto.admin.PageResponseDTO;
import com.lotteon.dto.product.ReviewDTO;
import com.lotteon.dto.product.ReviewRequestDTO;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.Review;
import com.lotteon.service.FileService;
import com.lotteon.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;


@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final ReviewService reviewService;
    private final FileService fileService;

    @GetMapping("/coupondetails")
    public String couponDetails(Model model) {
        model.addAttribute("content", "coupondetails");
        return "content/user/coupondetails"; // Points to "content/user/coupondetails"
    }

    @GetMapping("/myInfo")
    public String myInfo(Model model) {
        List<Review> recentReviews = reviewService.getRecentReviews(); // 최신 3개의 리뷰 가져오기
        model.addAttribute("recentReviews", recentReviews);
        model.addAttribute("content", "myInfo");
        return "content/user/mypageMain"; // Points to "content/user/mypageMain"
    }

    @ResponseBody
    @PostMapping("/myInfo/review")
    public ResponseEntity<?> submitReview(@ModelAttribute ReviewRequestDTO reviewDTO) {
        try {
            // ReviewService의 saveReview 메서드에 reviewDTO를 직접 전달
            boolean isSaved = reviewService.saveReview(reviewDTO);

            if (isSaved) {
                return ResponseEntity.ok().body(Collections.singletonMap("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.singletonMap("success", false));
            }
        } catch (Exception e) {
            e.printStackTrace(); // 오류 메시지 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("success", false));
        }
    }

    @GetMapping("/mysettings")
    public String mySettings(Model model) {
        model.addAttribute("content", "mysettings");
        return "content/user/mysettings"; // Points to "content/user/mysettings"
    }

    @GetMapping("/orderdetails")
    public String orderDetails(Model model) {
        model.addAttribute("content", "orderdetails");
        return "content/user/orderdetails"; // Points to "content/user/orderdetails"
    }

    @GetMapping("/pointdetails")
    public String pointDetails(Model model) {
        model.addAttribute("content", "pointdetails");
        return "content/user/pointdetails"; // Points to "content/user/pointdetails"
    }

    @GetMapping("/qnadetails")
    public String qnaDetails(Model model) {
        model.addAttribute("content", "qnadetails");
        return "content/user/qnadetails"; // Points to "content/user/qnadetails"
    }

    @GetMapping("/reviewdetails")
    public String reviewDetails(Model model, PageRequestDTO pageRequestDTO) {

        PageResponseDTO<ReviewDTO> pageResponseReviewDTO = reviewService.getAllReviewss(pageRequestDTO);
        model.addAttribute("pageResponseReviewDTO", pageResponseReviewDTO);

        List<Review> recentReviews = reviewService.getAllReviews();
        model.addAttribute("recentReviews", recentReviews);
        model.addAttribute("content", "reviewdetails");
        return "content/user/reviewdetails"; // Points to "content/user/reviewdetails"
    }

}
