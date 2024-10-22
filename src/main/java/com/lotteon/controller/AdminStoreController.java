package com.lotteon.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/store")
public class AdminStoreController {

    @GetMapping("/shoplist")
    public String adminShoplist(Model model) {
        model.addAttribute("cate", "store");
        model.addAttribute("content", "shoplist");
        return "content/admin/shop/admin_shoplist";
    }

    @GetMapping("/sale")
    public String adminShopsales(Model model) {
        model.addAttribute("cate", "store");
        model.addAttribute("content", "sale");
        return "content/admin/shop/admin_shopsales";
    }
}
