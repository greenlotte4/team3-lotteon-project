package com.lotteon.controller;


import com.lotteon.dto.product.ProductRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/seller")
public class SellerController {


    @GetMapping("/login")
    public String login(Model model) {
        return "content/admin/adminLogin";
    }

    @GetMapping("/{cate}/{content}")
    public String seller(@PathVariable String content,@PathVariable String cate, Model model) {

        model.addAttribute("content", content);
        model.addAttribute("cate", cate);

        return "sellerIndex";
    }

    @PostMapping("/product/register")
    public String insertProduct(@RequestParam ProductRequestDTO productRequestDTO, Model model) {
        log.info(productRequestDTO);
        return "content/admin/adminRegister";
    }


}
