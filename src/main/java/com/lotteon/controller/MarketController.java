package com.lotteon.controller;

import com.lotteon.dto.User.MemberDTO;
import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.dto.admin.PageResponseDTO;
import com.lotteon.dto.order.OrderCompletedResponseDTO;
import com.lotteon.dto.order.OrderDTO;
import com.lotteon.dto.order.OrderResponseDTO;
import com.lotteon.dto.product.*;
import com.lotteon.dto.product.cart.CartSummary;
import com.lotteon.dto.product.request.BuyNowRequestDTO;
import com.lotteon.dto.order.OrderRequestDTO;
import com.lotteon.entity.User.User;
import com.lotteon.entity.admin.CouponIssued;
import com.lotteon.entity.cart.Cart;
import com.lotteon.entity.cart.CartItem;
import com.lotteon.entity.product.ProductCategory;
import com.lotteon.entity.product.Review;
import com.lotteon.repository.product.ProductOptionCombinationRepository;
import com.lotteon.security.MyUserDetails;
import com.lotteon.service.AdminService;
import com.lotteon.service.ReviewService;
import com.lotteon.service.admin.CouponIssuedService;
import com.lotteon.service.order.OrderService;
import com.lotteon.service.product.MarketCartService;
import com.lotteon.service.product.ProductCategoryService;
import com.lotteon.service.product.ProductService;
import com.lotteon.service.user.CouponDetailsService;
import com.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
/*
    2024.10.28 하진희 - marketorder => 구매하기 버튼 기능 추가 (/buynow)
 */

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/market")
public class MarketController {

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private final UserService userService;
    private final CouponDetailsService couponDetailsService;
    private final MarketCartService marketCartService;
    private final ReviewService reviewService;
    private final OrderService orderService;
    private final AdminService adminService;
    private final ProductOptionCombinationRepository productOptionCombinationRepository;

    @GetMapping("/main/{category}")
    public String marketMain(Model model,@PathVariable long category) {
        ProductCategoryDTO categoryDTOs =  productCategoryService.getCategoryById(category);
        List<BannerDTO> banners = adminService.selectAllbanner();
        List<BannerDTO> banners2 = adminService.getActiveBanners();
        log.info(categoryDTOs);
        model.addAttribute("categoryDTOs",categoryDTOs);
        model.addAttribute("active",category);
        model.addAttribute("content", "main");
        model.addAttribute("banners", banners2);
        return "content/market/marketMain"; // Points to the "content/market/marketMain" template
    }

    @GetMapping("/list/{category}")
    public String marketList(PageRequestDTO pageRequestDTO,@PathVariable long category
            , @RequestParam(required = false, defaultValue = "popularity") String sort
             ,@RequestParam(required = false,defaultValue = "1") int page
            ,Model model) {
        pageRequestDTO.setCategoryId(category);
        pageRequestDTO.setPage(page);
        log.debug("Debugging category: " + category);

        List<ProductCategoryDTO> categoryDTOs =  productCategoryService.getAllParentCategoryDTOs(category);
        log.info("@222222222222222222222"+categoryDTOs);

        log.info("11111111111111"+pageRequestDTO.getCategoryId());
        log.info("category:"+category);
//        log.info("dsdsdsdsd2222"+pageRequestDTO);
        ProductListPageResponseDTO responseDTO =  productService.getSortProductList(pageRequestDTO,sort);
        log.info("controlllermarket::::"+responseDTO.getProductSummaryDTOs());
        model.addAttribute("categoryDTOs",categoryDTOs);
        model.addAttribute("responseDTO",responseDTO);
        model.addAttribute("sort", sort);


        return "content/market/marketList"; // Points to the "content/market/marketList" template
    }

    @GetMapping("/search")
    public String marketSearch(Model model) {
        model.addAttribute("content", "search");
        return "content/market/marketSearch"; // Points to the "content/market/marketSearch" template
    }

    @GetMapping("/view")
    public String marketView(Model model) {
        model.addAttribute("content", "view");



        return "content/market/marketview"; // Points to the "content/market/marketview" template
    }

    @GetMapping("/view/{categoryId}/{productId}")
    public String marketView(@PathVariable Long productId,@PathVariable Long categoryId,Model model, com.lotteon.dto.admin.PageRequestDTO pageRequestDTO) {
        log.info(productId);
        log.info(categoryId);

        pageRequestDTO.setSize(6);
        //선택시 hit update
        productService.updatehit(productId);

        List<ProductCategoryDTO> categoryDTOs =  productCategoryService.selectCategory(categoryId);
        log.info("categories LLLLL "+ categoryDTOs);
        ProductDTO productdto = productService.getProduct(productId);
        log.info("productVIew Controller:::::"+productdto);


        PageResponseDTO<ReviewDTO> pageResponseReviewDTO = reviewService.getAllReviewsss(pageRequestDTO, productId);
        model.addAttribute("pageResponseReviewDTO", pageResponseReviewDTO);

        List<Review> ReviewImgs = reviewService.getAllReviews();


        model.addAttribute("reviewImgs", ReviewImgs);

        model.addAttribute("categoryDTOs",categoryDTOs);
        model.addAttribute("products",productdto);

        return "content/market/marketview"; // Points to the "content/market/marketview" template
    }


    @GetMapping("/cart")
    public String marketCart(Model model) {


        List<CartItem> cartItems = marketCartService.selectCartAll();

        CartSummary cartSummary = marketCartService.calculateSelectedCartSummary(cartItems);

        model.addAttribute("cartItems",cartItems);
        model.addAttribute("cartSummary",cartSummary);
        log.info("카트 총집합! cart items: {}", cartItems);

        return "content/market/marketcart"; // Points to the "content/market/marketcart" template
    }


    @GetMapping("/order/{uid}")
    public String marketOrder(@PathVariable String uid,Model model, String productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String memberId = (userDetails.getId());  // 로그인한 사용자의 Member ID (String 타입)

        log.info("uid ::::::::::"+uid);
        MemberDTO memberDTO = userService.getByUsername(uid);



        log.info("멤버 아이디다"+memberId);

        // 해당 멤버의 발급된 쿠폰 목록 조회

        List<CouponIssued> issuedCoupons = couponDetailsService.memberOrderCouponList(memberId, productId); // 서비스에서 발급된 쿠폰 조회
        log.info("발급받은 쿠폰: {}", issuedCoupons);

        model.addAttribute("issuedList", issuedCoupons);



        log.info(memberDTO);
        model.addAttribute("memberDTO",memberDTO);
        model.addAttribute("productId", productId);

        return "content/market/marketorder"; // Points to the "content/market/marketorder" template
    }

    @PostMapping("/order/saveOrder")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> saveOrder(@RequestBody OrderRequestDTO orderRequestDTO, Authentication authentication){
        Map<String, Long> response = new HashMap<>();
        response.put("result", 0L);
        log.info("요기!!!!!!!!!!!!!!!!!"+orderRequestDTO);
        OrderResponseDTO orderResponseDTO  = new OrderResponseDTO(orderRequestDTO);
        long orderId = orderService.saveOrder(orderResponseDTO);


        if(orderId>0){
            response.put("result",orderId);

        }


        return ResponseEntity.ok(response);
    }


    @PostMapping("/buyNow")
    @ResponseBody
    public ResponseEntity<Map<String, String>> processOrder(@RequestBody List<BuyNowRequestDTO> productDataList, Authentication authentication) {
        Map<String, String> response = new HashMap<>();

        // Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("result", "login_required");
            return ResponseEntity.ok(response);
        }

        // Check if product data list is provided
        if (productDataList == null || productDataList.isEmpty()) {
            response.put("result", "fail");
            return ResponseEntity.ok(response);
        }

        // Retrieve user details using authentication
        String uid = authentication.getName();
        Optional<User> opt = userService.findUserByUid(uid);

        if (opt.isPresent()) {
            User user = opt.get();

            // Check if the user role is not allowed for purchase
            if (user.getRole() != User.Role.MEMBER) {
                response.put("result", "auth"); // Admin or seller accounts cannot purchase
                return ResponseEntity.ok(response);
            }

            // Process each product item (additional business logic can be added here)
            for (BuyNowRequestDTO productData : productDataList) {
                // Here, implement any logic needed for each product in the order
                System.out.println("Processing order for product: " + productData.getProductName());
            }

            // Purchase is successful for member role
            response.put("result", "success");


        } else {
            // No user account found
            response.put("result", "none");
        }

        return ResponseEntity.ok(response);

    }

    @GetMapping("/completed/{orderId}")
    public String marketOrderCompleted(@PathVariable long orderId,Model model) {
        model.addAttribute("content", "completed");
        OrderCompletedResponseDTO orderDTO = orderService.selectOrderById(orderId);
        log.info("여기!!!!!!!!!!!!!!!! : "+orderDTO);
        model.addAttribute("orderDTO",orderDTO);


        return "content/market/marketorderCompleted"; // Points to the "content/market/marketorderCompleted" template
    }

    @PostMapping("/cart/cartOrder/{cartId}")
    public ResponseEntity<Cart> cartOrder(
            @PathVariable long cartId,
            @RequestBody List<BuyNowRequestDTO> cartOrders
    ){
        log.info("상품 주문 오더 들어왔다");
        for (BuyNowRequestDTO cartOrder : cartOrders) {
            log.info("오더들"+cartOrder);
        }

        return ResponseEntity.ok().build();
    }
}
