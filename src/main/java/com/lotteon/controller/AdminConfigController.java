package com.lotteon.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/config")
public class AdminConfigController {

    @GetMapping("/banner")
    public String adminBanner(Model model) {
        model.addAttribute("cate", "config");
        model.addAttribute("content", "banner");
        return "content/admin/config/admin_Banner";
    }

    @GetMapping("/basic")
    public String adminBasic(Model model) {
        model.addAttribute("cate", "config");
        model.addAttribute("content", "basic");
        return "content/admin/config/admin_basic";
    }

    @GetMapping("/terms")
    public String adminTerms(Model model) {
        model.addAttribute("cate", "config");
        model.addAttribute("content", "terms");
        return "content/admin/config/admin_Terms";
    }

    @GetMapping("/version")
    public String adminVersion(Model model) {
        model.addAttribute("cate", "config");
        model.addAttribute("content", "version");
        return "content/admin/config/admin_Version";
    }
}
