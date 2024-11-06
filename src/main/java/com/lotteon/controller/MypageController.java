package com.lotteon.controller;

import com.lotteon.dto.QnaDTO;
import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.dto.admin.PageRequestDTO;
import com.lotteon.dto.admin.PageResponseDTO;
import com.lotteon.dto.product.ReviewDTO;
import com.lotteon.dto.product.ReviewRequestDTO;
import com.lotteon.entity.QnA;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.Review;
import com.lotteon.repository.QnaRepository;
import com.lotteon.security.MyUserDetails;
import com.lotteon.service.AdminService;
import com.lotteon.service.FileService;
import com.lotteon.service.ReviewService;
import com.lotteon.service.admin.CouponIssuedService;
import com.lotteon.service.user.CouponDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final ReviewService reviewService;
    private final FileService fileService;
    private final AdminService adminService;
    private final CouponDetailsService couponDetailsService;
    private final CouponIssuedService couponIssuedService;
    private final QnaRepository qnaRepository;

    @GetMapping("/coupondetails")
    public String couponDetails(Model model) {
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();
        log.info("쿠폰디테일 요청");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String memberId = (userDetails.getId());  // 로그인한 사용자의 Member ID (String 타입)

        log.info("멤버 아이디다"+memberId);

        // 해당 멤버의 발급된 쿠폰 목록 조회

            List<CouponIssued> issuedCoupons = couponDetailsService.memberCouponList(memberId); // 서비스에서 발급된 쿠폰 조회
            log.info("발급받은 쿠폰: {}", issuedCoupons);

            model.addAttribute("IssuedList", issuedCoupons);


        model.addAttribute("banners", banners);
        return "content/user/coupondetails"; // Points to "content/user/coupondetails"
    }

    @GetMapping("/myInfo")
    public String myInfo(Model model) {
        List<Review> recentReviews = reviewService.getRecentReviews(); // 최신 3개의 리뷰 가져오기
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();
        model.addAttribute("recentReviews", recentReviews);
        model.addAttribute("content", "myInfo");
        model.addAttribute("banners", banners2);
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
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();
        model.addAttribute("content", "mysettings");
        model.addAttribute("banners", banners2);
        return "content/user/mysettings"; // Points to "content/user/mysettings"
    }

    @GetMapping("/orderdetails")
    public String orderDetails(Model model) {
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();
        model.addAttribute("content", "orderdetails");
        model.addAttribute("banners", banners2);
        return "content/user/orderdetails"; // Points to "content/user/orderdetails"
    }

    @GetMapping("/pointdetails")
    public String pointDetails(Model model) {
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();
        model.addAttribute("content", "pointdetails");
        model.addAttribute("banners", banners2);
        return "content/user/pointdetails"; // Points to "content/user/pointdetails"
    }

    @GetMapping("/qnadetails")
    public String qnaDetails(
            @RequestParam(value = "cate", required = false) String category,
            Authentication authentication, Model model,
            @PageableDefault(size = 10, sort = "rdate", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {

        // 배너 데이터 가져오기
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();

        // 페이지가 첫 번째 페이지일 경우 1페이지로 리다이렉트
        if (pageable.getPageNumber() == 0) {
            pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "rdate"));
        }

        // QnA 조회 로직 설정
        String requestURI = request.getRequestURI();
        Page<QnA> qnaPage = getQnaPage(requestURI, category, authentication, pageable);

        // 모델에 데이터 추가
        model.addAttribute("content", "qnadetails");
        model.addAttribute("banners", banners2);
        model.addAttribute("qnaPage", qnaPage);
        model.addAttribute("selectedCategory", category);

        return "content/user/qnadetails";
    }

    private Page<QnA> getQnaPage(String requestURI, String category, Authentication authentication, Pageable pageable) {
        if ("/mypage/qnadetails".equals(requestURI)) {
            // 마이페이지에서 접근한 경우, 현재 사용자 게시물만 조회
            String uid = authentication.getName();
            return qnaRepository.findByQnaWriter(uid, pageable);
        } else if (category != null) {
            // 특정 카테고리 조회
            return qnaRepository.findByQna_type1(category, pageable);
        } else {
            // 전체 조회
            return qnaRepository.findAll(pageable);
        }
    }


    @GetMapping("/reviewdetails")
    public String reviewDetails(Model model, PageRequestDTO pageRequestDTO) {
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();
        PageResponseDTO<ReviewDTO> pageResponseReviewDTO = reviewService.getAllReviewss(pageRequestDTO);
        log.info("aaaaaaa{}", pageResponseReviewDTO);
        model.addAttribute("pageResponseReviewDTO", pageResponseReviewDTO);

        List<Review> recentReviews = reviewService.getAllReviews();
        model.addAttribute("recentReviews", recentReviews);
        model.addAttribute("content", "reviewdetails");
        model.addAttribute("banners", banners2);
        return "content/user/reviewdetails"; // Points to "content/user/reviewdetails"
    }

}
