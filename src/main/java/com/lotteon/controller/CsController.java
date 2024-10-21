package com.lotteon.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/cs")
public class CsController {


    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("cate", "main");
        return "csIndex";
    }


    @GetMapping("/{cate}/{content}")
    public String cs(@PathVariable String content, @PathVariable String cate, Model model){
        model.addAttribute("cate", cate);
        model.addAttribute("content", content);
        return "csIndex";
    }





}
