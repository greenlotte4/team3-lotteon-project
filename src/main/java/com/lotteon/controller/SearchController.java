package com.lotteon.controller;


import com.lotteon.dto.product.PageRequestDTO;
import com.lotteon.dto.product.ProductListPageResponseDTO;
import com.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@Controller
public class SearchController {

    private final ProductService productService;


    @ResponseBody
    @GetMapping("/api/search")
    public ProductListPageResponseDTO marketHeaderSearch(
            @RequestParam String query
            ,PageRequestDTO pageRequestDTO
            ,Model model) {
        log.info("query!!!"+query);

        pageRequestDTO.setType("productName");
        pageRequestDTO.setKeyword(query);
        ProductListPageResponseDTO productPageResponseDTO =productService.SearchProductAll(pageRequestDTO,query);
        log.info("확인!!!!!!!!"+productPageResponseDTO);
//        model.addAttribute("productPageResponseDTO", productPageResponseDTO);
////        model.addAttribute("query", query);

        return productPageResponseDTO;
    }

    @GetMapping("/market/search")
    public String marketSearch(    @RequestParam String query
            ,PageRequestDTO pageRequestDTO,Model model) {
        log.info("query!!!"+query);

        pageRequestDTO.setType("productName");
        pageRequestDTO.setKeyword(query);

        ProductListPageResponseDTO products =productService.SearchProductAll(pageRequestDTO,query);
        log.info("확인!!!!!!!!"+products);

        model.addAttribute("products", products);
        model.addAttribute("query", query);

        model.addAttribute("content", "search");
        return "content/market/marketSearch"; // Points to the "content/market/marketSearch" template
    }


}
