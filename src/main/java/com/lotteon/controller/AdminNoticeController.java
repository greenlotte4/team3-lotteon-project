package com.lotteon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/notice")
public class AdminNoticeController {

    @GetMapping("/list")
    public String adminNoticeList(Model model) {
        model.addAttribute("cate", "notice");
        model.addAttribute("content", "list");
        return "content/admin/notice/noticeList";
    }

    @GetMapping("/modify")
    public String adminNoticeModify(Model model) {
        model.addAttribute("cate", "notice");
        model.addAttribute("content", "modify");
        return "content/admin/notice/noticeModify";
    }

    @GetMapping("/view")
    public String adminNoticeView(Model model) {
        model.addAttribute("cate", "notice");
        model.addAttribute("content", "view");
        return "content/admin/notice/noticeView";
    }

    @GetMapping("/write")
    public String adminNoticeWrite(Model model) {
        model.addAttribute("cate", "notice");
        model.addAttribute("content", "write");
        return "content/admin/notice/noticeWrite";
    }

}
