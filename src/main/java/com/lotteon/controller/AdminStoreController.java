package com.lotteon.controller;


import com.lotteon.entity.User.Seller;
import com.lotteon.service.user.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/store")
@RequiredArgsConstructor
public class AdminStoreController {

    private final SellerService sellerService;

    @GetMapping("/shoplist")
    public String adminShoplist(Model model) {

        model.addAttribute("cate", "store");
        model.addAttribute("content", "shoplist");

        List<Seller> sellerList = sellerService.getAllSellers();
        model.addAttribute("sellerList", sellerList);

        return "content/admin/shop/admin_shoplist";
    }

    @PostMapping("/deletesellers")
    public ResponseEntity<String> deleteSellers(@RequestBody List<Long> sellerIds) {
        System.out.println("Received seller IDs: " + sellerIds); // 로그 출력
        try {
            sellerService.deleteSellersByIds(sellerIds);
            return ResponseEntity.ok("선택한 판매자가 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제에 실패했습니다.");
        }
    }


    @GetMapping("/sale")
    public String adminShopsales(Model model) {
        model.addAttribute("cate", "store");
        model.addAttribute("content", "sale");
        return "content/admin/shop/admin_shopsales";
    }
}
