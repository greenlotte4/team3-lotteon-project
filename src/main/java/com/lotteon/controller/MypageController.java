package com.lotteon.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@Log4j2
@RequiredArgsConstructor
public class MypageController {

    @GetMapping("/mypage/{content}")
    public String join(@PathVariable String content, Model model){
        model.addAttribute("content", content);
        return "userIndex";
    }

}
