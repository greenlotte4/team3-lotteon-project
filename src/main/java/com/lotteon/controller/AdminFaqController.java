package com.lotteon.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/faq")
public class AdminFaqController {

    @GetMapping("/list")
    public String adminFaqList(Model model) {
        model.addAttribute("cate", "faq");
        model.addAttribute("content", "list");
        return "content/admin/faq/faqList";
    }

    @GetMapping("/modify")
    public String adminFaqModify(Model model) {
        model.addAttribute("cate", "faq");
        model.addAttribute("content", "modify");
        return "content/admin/faq/faqModify";
    }

    @GetMapping("/view")
    public String adminFaqView(Model model) {
        model.addAttribute("cate", "faq");
        model.addAttribute("content", "view");
        return "content/admin/faq/faqView";
    }

    @GetMapping("/write")
    public String adminFaqWrite(Model model) {
        model.addAttribute("cate", "faq");
        model.addAttribute("content", "write");
        return "content/admin/faq/faqWrite";
    }
}
