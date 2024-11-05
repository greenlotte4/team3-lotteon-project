package com.lotteon.controller;

import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
public class MainController {


    private final AdminService adminService;

    @GetMapping("/")
    public String index(Model model){

        List<BannerDTO> banners = adminService.selectAllbanner();
        log.info("gdgd :" + banners);
        model.addAttribute("banners", banners);
        return "mainIndex";
    }


    @GetMapping("/category")
    public String category(){
        return "category";
    }



}
