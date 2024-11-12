package com.lotteon.controller;

import com.lotteon.dto.BoardCateDTO;
import com.lotteon.dto.User.MemberDTO;
import com.lotteon.dto.User.UserDTO;
import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.dto.admin.PageRequestDTO;
import com.lotteon.dto.admin.PageResponseDTO;
import com.lotteon.dto.adminQnaDTO;
import com.lotteon.dto.order.OrderDTO;
import com.lotteon.dto.order.OrderItemDTO;
import com.lotteon.dto.order.OrderWithGroupedItemsDTO;
import com.lotteon.dto.page.OrderPageResponseDTO;
import com.lotteon.dto.page.PointPageRequestDTO;
import com.lotteon.dto.page.PointPageResponseDTO;
import com.lotteon.dto.page.QnaPageResponseDTO;
import com.lotteon.dto.product.ReviewDTO;
import com.lotteon.dto.product.ReviewRequestDTO;
import com.lotteon.entity.QnA;
import com.lotteon.entity.User.Member;
import com.lotteon.entity.User.Point;
import com.lotteon.entity.admin.Adminqna;
import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.entity.order.OrderItem;
import com.lotteon.entity.product.Review;
import com.lotteon.repository.QnaRepository;
import com.lotteon.repository.admin.AdminQnaRepository;
import com.lotteon.security.MyUserDetails;
import com.lotteon.service.AdminService;
import com.lotteon.service.BoardService;
import com.lotteon.service.FileService;
import com.lotteon.service.ReviewService;
import com.lotteon.service.admin.CouponIssuedService;
import com.lotteon.service.admin.QnaService;
import com.lotteon.service.order.OrderService;
import com.lotteon.service.user.CouponDetailsService;
import com.lotteon.service.user.MemberService;
import com.lotteon.service.user.PointService;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


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
    private final OrderService orderService;
    private final AdminQnaRepository adminQnaRepository;
    private final MemberService memberService;
    private final QnaService qnaService;
    private final BoardService boardService;
    private final PointService pointService;

    @GetMapping("/coupondetails")
    public String couponDetails(Model model) {
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();
        log.info("쿠폰디테일 요청");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String memberId = (userDetails.getId());  // 로그인한 사용자의 Member ID (String 타입)

        log.info("멤버 아이디다" + memberId);

        // 해당 멤버의 발급된 쿠폰 목록 조회

        List<CouponIssued> issuedCoupons = couponDetailsService.memberCouponList(memberId); // 서비스에서 발급된 쿠폰 조회
        log.info("발급받은 쿠폰: {}", issuedCoupons);

        model.addAttribute("IssuedList", issuedCoupons);


        model.addAttribute("banners", banners2);
        return "content/user/coupondetails"; // Points to "content/user/coupondetails"
    }

//    @GetMapping("confirmPoint")
//    public String confirmPoint(Model model) {
//
//
//    }

    @GetMapping("/myInfo")
    public String myInfo(Model model, Authentication authentication) {
        String uid = authentication.getName();
        List<Review> recentReviews = reviewService.getRecentReviewsByUser(uid); // 최신 3개의 리뷰 가져오기
        List<BannerDTO> banners2 = adminService.getActiveBanners();

        // 주문을 orderId로 그룹화
        List<OrderItemDTO> groupDTO = orderService.getOrdersGroupedByOrderItemId(uid);

        model.addAttribute("groupDTO", groupDTO); // 그룹화된 데이터 전달
        model.addAttribute("recentReviews", recentReviews);
        model.addAttribute("content", "myInfo");
        model.addAttribute("banners", banners2);
        return "content/user/mypageMain"; // "mypageMain" 뷰로 이동
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String uid = (userDetails.getId());  // 로그인한 사용자의 Member ID (String 타입)

        Member member = memberService.findByUserId(uid)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        model.addAttribute("member", member);

        return "content/user/mysettings"; // Points to "content/user/mysettings"
    }

    @PostMapping("/mysettings/update")
    public ResponseEntity<Map<String, String>> updateMember(
            @RequestBody MemberDTO memberData) {

        Optional<Member> existingMemberOpt = memberService.findByUserId(memberData.getUid());

        if (existingMemberOpt.isPresent()) {
            Member existingMember = existingMemberOpt.get();

            // 업데이트된 값을 기존 Member 객체에 반영
            existingMember.setName(memberData.getName());
            existingMember.setEmail(memberData.getEmail());
            existingMember.setHp(memberData.getHp());
            existingMember.setPostcode(memberData.getPostcode());
            existingMember.setAddr(memberData.getAddr());
            existingMember.setAddr2(memberData.getAddr2());

            // 업데이트 메서드 호출
            memberService.updateMember(existingMember.getId(), existingMember);

            // 성공 메시지 반환
            Map<String, String> response = new HashMap<>();
            response.put("message", "회원 정보가 성공적으로 업데이트되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            // 사용자가 존재하지 않는 경우
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }




    @GetMapping("/orderdetails")
    public String orderDetails(Model model, PageRequestDTO pageRequestDTO, Authentication authentication) {
        String uid = authentication.getName();
        List<BannerDTO> banners2 = adminService.getActiveBanners();

        // 변경된 메서드 호출
        OrderPageResponseDTO<OrderItemDTO> pageResponseOrderItemDTO = orderService.getOrderByUserItems(pageRequestDTO, uid);

        List<BoardCateDTO> boardCateDTOS = boardService.selectBoardCate();
        List<String> targetNames = Arrays.asList("주문/결제", "배송", "여행/숙박/항공");
        List<BoardCateDTO> filteredBoardCateDTOS = boardCateDTOS.stream()
                .filter(cate -> targetNames.contains(cate.getName()))
                .collect(Collectors.toList());

        List<OrderItemDTO> groupDTO = orderService.getOrdersGroupedByOrderItemId(uid);

        model.addAttribute("groupDTO", groupDTO);
        model.addAttribute("boardCate", filteredBoardCateDTOS);
        model.addAttribute("pageResponseOrderDTO", pageResponseOrderItemDTO);  // OrderItemDTO로 변경된 DTO 전달
        model.addAttribute("content", "orderdetails");
        model.addAttribute("banners", banners2);
        return "content/user/orderdetails";
    }

    @GetMapping("/pointdetails")
    public String pointDetails(Model model, Pageable pageable) {
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();
        model.addAttribute("content", "pointdetails");
        model.addAttribute("banners", banners2);

        // 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String memberId = userDetails.getId();

        // 포인트 리스트 조회 (페이징 적용)
        Page<Point> pointPage = pointService.myPoints(memberId, pageable);

        // 총 포인트 계산
        double totalPoints = pointService.getTotalPoints(memberId);

        PointPageRequestDTO requestDTO = PointPageRequestDTO.builder()
                .pg(pageable.getPageNumber() + 1)  // 페이지 번호는 0부터 시작하므로 1을 더함
                .size(pageable.getPageSize())      // 페이지 크기
                .build();

        PointPageResponseDTO responseDTO = new PointPageResponseDTO(
                requestDTO,  // 요청 DTO를 전달
                pointPage.getContent(),  // 포인트 목록
                pointPage.getTotalElements(),  // 전체 항목 수 (long 타입)
                pageable.getPageNumber() + 1,  // 현재 페이지 번호 (1부터 시작하므로 +1)
                pageable.getPageSize()  // 페이지 사이즈
        );


        // 모델에 추가
        model.addAttribute("pageResponseDTO", responseDTO);
        model.addAttribute("totalPoints", totalPoints);
        model.addAttribute("pointList", pointPage.getContent());

        log.info("나온 결과 맴버 포인트" + pointPage);
        log.info("Point List: " + pointPage.getContent());
        log.info("Page Response DTO: " + responseDTO);
        return "content/user/pointdetails"; // Points to "content/user/pointdetails"
    }


    @GetMapping("/qnadetails")
    public String qnaDetails(
            @RequestParam(value = "cate", required = false) String category,
            Authentication authentication, Model model,
            @PageableDefault(size = 10, sort = "date", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest request) {

        // 배너 데이터 가져오기
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();

        // 페이지가 첫 번째 페이지일 경우 1페이지로 리다이렉트
        if (pageable.getPageNumber() == 0) {
            pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "date"));
        }

        String loggedInUserUid = authentication.getName();  // 로그인한 사용자의 ID
        log.info("login!!!!"+loggedInUserUid);


        // QnA 조회 로직 설정
        com.lotteon.dto.page.PageRequestDTO pageRequestDTO= com.lotteon.dto.page.PageRequestDTO.builder()
                .qnawriter(loggedInUserUid)
                .build();

        QnaPageResponseDTO qnaPageResponseDTO = qnaService.selectQnaListAll(pageRequestDTO);

//        String requestURI = request.getRequestURI();
//        Page<adminQnaDTO> qnaPage = adminService.getQnaPage(requestURI, category, authentication, pageable);

        log.info("여기!!!!!!!!!?"+qnaPageResponseDTO);
        // 모델에 데이터 추가
        model.addAttribute("content", "qnadetails");
        model.addAttribute("banners", banners2);
        model.addAttribute("qnaPage", qnaPageResponseDTO);
        model.addAttribute("selectedCategory", category);

        return "content/user/qnadetails";
    }


    @GetMapping("/reviewdetails")
    public String reviewDetails(Model model, PageRequestDTO pageRequestDTO, Authentication authentication) {
        String uid = authentication.getName();
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();

        // 로그인한 유저의 리뷰만 가져오기
        PageResponseDTO<ReviewDTO> pageResponseReviewDTO = reviewService.getReviewsByUser(pageRequestDTO, uid);
        log.info("aaaaaaa{}", pageResponseReviewDTO);


        model.addAttribute("pageResponseReviewDTO", pageResponseReviewDTO);
        model.addAttribute("content", "reviewdetails");
        model.addAttribute("banners", banners2);
        return "content/user/reviewdetails"; // Points to "content/user/reviewdetails"
    }

    @GetMapping("/mypage/primary")
    public ResponseEntity<String> handlePrimaryAction(@RequestParam("uid") String uid) {
        log.info("요청 들어왔다 수취확인");
        // uid를 사용하여 필요한 비즈니스 로직 수행

        // 로직 수행 후 적절한 응답 반환
        return ResponseEntity.ok("성공적으로 요청을 처리했습니다.");
    }

}
