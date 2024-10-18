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
public class MemberController {



    @GetMapping("/member/{content}")
    public String join(@PathVariable String content, Model model){
        model.addAttribute("content", content);
        return "memberIndex";
    }

}
