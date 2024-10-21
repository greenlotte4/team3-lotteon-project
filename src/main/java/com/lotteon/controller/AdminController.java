package com.lotteon.controller;

import lombok.Builder;
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
@RequestMapping("/admin")
public class AdminController {


    @GetMapping("/main")
    public String adminMain( Model model) {

        model.addAttribute("cate", "main");
        return "adminIndex";
    }

    @GetMapping("/{cate}/{content}")
    public String admin(@PathVariable String content, @PathVariable String cate, Model model) {

        model.addAttribute("cate", cate);
        model.addAttribute("content", content);
        log.info(content);
        log.info("cate"+cate);
        return "adminIndex";
    }



}
