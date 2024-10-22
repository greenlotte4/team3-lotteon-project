package com.lotteon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/qna")
public class AdminQnaController {

    @GetMapping("/list")
    public String adminQnaList(Model model) {
        model.addAttribute("cate", "qna");
        model.addAttribute("content", "list");
        return "content/admin/qna/qnaList";
    }

    @GetMapping("/reply")
    public String adminQnaReply(Model model) {
        model.addAttribute("cate", "qna");
        model.addAttribute("content", "reply");
        return "content/admin/qna/qnaReply";
    }

    @GetMapping("/write")
    public String adminQnaWrite(Model model) {
        model.addAttribute("cate", "qna");
        model.addAttribute("content", "write");
        return "content/admin/qna/qnaWrite";
    }
}

