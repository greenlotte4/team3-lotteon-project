package com.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    @GetMapping("/coupondetails")
    public String couponDetails(Model model) {
        model.addAttribute("content", "coupondetails");
        return "content/user/coupondetails"; // Points to "content/user/coupondetails"
    }

    @GetMapping("/myInfo")
    public String myInfo(Model model) {
        model.addAttribute("content", "myInfo");
        return "content/user/mypageMain"; // Points to "content/user/mypageMain"
    }

    @GetMapping("/mysettings")
    public String mySettings(Model model) {
        model.addAttribute("content", "mysettings");
        return "content/user/mysettings"; // Points to "content/user/mysettings"
    }

    @GetMapping("/orderdetails")
    public String orderDetails(Model model) {
        model.addAttribute("content", "orderdetails");
        return "content/user/orderdetails"; // Points to "content/user/orderdetails"
    }

    @GetMapping("/pointdetails")
    public String pointDetails(Model model) {
        model.addAttribute("content", "pointdetails");
        return "content/user/pointdetails"; // Points to "content/user/pointdetails"
    }

    @GetMapping("/qnadetails")
    public String qnaDetails(Model model) {
        model.addAttribute("content", "qnadetails");
        return "content/user/qnadetails"; // Points to "content/user/qnadetails"
    }

    @GetMapping("/reviewdetails")
    public String reviewDetails(Model model) {
        model.addAttribute("content", "reviewdetails");
        return "content/user/reviewdetails"; // Points to "content/user/reviewdetails"
    }

}
