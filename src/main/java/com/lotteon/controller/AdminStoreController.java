package com.lotteon.controller;


import com.lotteon.entity.User.Seller;
import com.lotteon.service.user.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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



    @GetMapping("/sale")
    public String adminShopsales(Model model) {
        model.addAttribute("cate", "store");
        model.addAttribute("content", "sale");
        return "content/admin/shop/admin_shopsales";
    }
}
