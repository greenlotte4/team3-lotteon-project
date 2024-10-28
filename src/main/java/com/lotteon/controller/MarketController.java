package com.lotteon.controller;

import com.lotteon.dto.product.*;
import com.lotteon.dto.product.request.BuyNowRequestDTO;
import com.lotteon.entity.User.User;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    public String marketView(@PathVariable long productId,@PathVariable long categoryId,Model model) {
        log.info(productId);
        log.info(categoryId);

       List<ProductCategoryDTO> categoryDTOs =  productCategoryService.selectCategory(categoryId);
       log.info("categories LLLLL "+ categoryDTOs);
       ProductDTO productdto = productService.getProduct(productId);
        log.info("productVIew Controller:::::"+productdto);

        model.addAttribute("categoryDTOs",categoryDTOs);
        model.addAttribute("products",productdto);

        return "content/market/marketview"; // Points to the "content/market/marketview" template
    }


    @GetMapping("/cart")
    public String marketCart(Model model) {
        model.addAttribute("content", "cart");
        return "content/market/marketcart"; // Points to the "content/market/marketcart" template
    }


    @GetMapping("/order")
    public String marketOrder(Model model) {

        return "content/market/marketorder"; // Points to the "content/market/marketorder" template
    }

    @PostMapping("/buyNow")
    @ResponseBody
    public ResponseEntity<Map<String, String>> processOrder(@RequestBody BuyNowRequestDTO productDTO, Authentication authentication) {
        Map<String, String> response = new HashMap<>();

        // Authentication 객체가 null인지 확인하고 인증 여부를 구분
        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("result", "login_required");
            return ResponseEntity.ok(response);
        }
        if(productDTO != null) {
            String uid= authentication.getName();
            Optional<User> opt= userService.findUserByUid(uid);
            if(opt.isPresent()) {
                User user=opt.get();
                if(user.getRole() != User.Role.MEMBER) {
                    //admin, seller계정은 구매 불가
                    response.put("result", "auth");
                    return ResponseEntity.ok(response);
                }
                //member계정 구매가능
                response.put("result", "success");

            }else{
            //계정이 없다.
            response.put("result", "none");
            }

        }else{
            //order정보가 없다.
            response.put("result", "fail");
        }
        return ResponseEntity.ok(response);

    }

    @GetMapping("/completed")
    public String marketOrderCompleted(Model model) {
        model.addAttribute("content", "completed");
        return "content/market/marketorderCompleted"; // Points to the "content/market/marketorderCompleted" template
    }

}
