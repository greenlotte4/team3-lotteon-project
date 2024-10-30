package com.lotteon.controller;

import com.lotteon.dto.User.MemberDTO;
import com.lotteon.dto.admin.PageResponseDTO;
import com.lotteon.dto.order.OrderResponseDTO;
import com.lotteon.dto.product.*;
import com.lotteon.dto.product.cart.CartSummary;
import com.lotteon.dto.product.request.BuyNowRequestDTO;
import com.lotteon.dto.order.OrderRequestDTO;
import com.lotteon.entity.User.User;
import com.lotteon.entity.cart.CartItem;
import com.lotteon.entity.product.Review;
import com.lotteon.service.ReviewService;
import com.lotteon.service.order.OrderService;
import com.lotteon.service.product.MarketCartService;
import com.lotteon.service.product.ProductCategoryService;
import com.lotteon.service.product.ProductService;
import com.lotteon.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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
    private final MarketCartService marketCartService;
    private final ReviewService reviewService;
    private final OrderService orderService;

    @GetMapping("/main")
    public String marketMain(Model model) {
        model.addAttribute("content", "main");
        return "content/market/marketMain"; // Points to the "content/market/marketMain" template
    }

    @GetMapping("/list")
    public String marketList(PageRequestDTO pageRequestDTO, Model model) {
        long categoryid = 3;
        pageRequestDTO.setCategoryId(categoryid);
        ProductListPageResponseDTO responseDTO =  productService.getProductList(pageRequestDTO);
        log.info("controlllermarket::::"+responseDTO.getProductDTOs());
        model.addAttribute("responseDTO",responseDTO);


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
    public String marketView(@PathVariable long productId,@PathVariable long categoryId,Model model, com.lotteon.dto.admin.PageRequestDTO pageRequestDTO) {
        log.info(productId);
        log.info(categoryId);

        pageRequestDTO.setSize(6);

       List<ProductCategoryDTO> categoryDTOs =  productCategoryService.selectCategory(categoryId);
       log.info("categories LLLLL "+ categoryDTOs);
       ProductDTO productdto = productService.getProduct(productId);
        log.info("productVIew Controller:::::"+productdto);

        PageResponseDTO<ReviewDTO> pageResponseReviewDTO = reviewService.getAllReviewss(pageRequestDTO);
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

        CartSummary cartSummary = marketCartService.calculateCartSummary(cartItems);

        model.addAttribute("cartItems",cartItems);
        model.addAttribute("cartSummary",cartSummary);
        log.info("카트 총집합! cart items: {}", cartItems);

        return "content/market/marketcart"; // Points to the "content/market/marketcart" template
    }


    @GetMapping("/order/{uid}")
    public String marketOrder(@PathVariable String uid,Model model) {

        log.info("uid ::::::::::"+uid);
        MemberDTO memberDTO = userService.getByUsername(uid);
        log.info(memberDTO);
        model.addAttribute("memberDTO",memberDTO);


        return "content/market/marketorder"; // Points to the "content/market/marketorder" template
    }

    @PostMapping("/order/saveOrder")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveOrder(@RequestBody OrderRequestDTO orderRequestDTO, Authentication authentication){

        log.info("요기!!!!!!!!!!!!!!!!!"+orderRequestDTO);
        OrderResponseDTO orderResponseDTO  = new OrderResponseDTO(orderRequestDTO);
        long orderId = orderService.saveOrder(orderResponseDTO);




        return null;
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

    @GetMapping("/completed")
    public String marketOrderCompleted(Model model) {
        model.addAttribute("content", "completed");
        return "content/market/marketorderCompleted"; // Points to the "content/market/marketorderCompleted" template
    }

}
