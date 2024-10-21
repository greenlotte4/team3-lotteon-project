package com.lotteon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

    @GetMapping("/list")
    public String adminUserList(Model model) {
        model.addAttribute("cate", "user");
        model.addAttribute("content", "list");
        return "content/admin/user/memberlist";
    }

    @GetMapping("/point")
    public String adminUserPoint(Model model) {
        model.addAttribute("cate", "user");
        model.addAttribute("content", "point");
        return "content/admin/user/memberpoint";
    }
}
