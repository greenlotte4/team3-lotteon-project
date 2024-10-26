package com.lotteon.controller;

import com.lotteon.dto.product.*;
import com.lotteon.service.product.ProductCategoryService;
import com.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/market")
public class MarketController {

    private final ProductService productService;
    private final ProductCategoryService productCategoryService;

    @GetMapping("/main")
    public String marketMain(Model model) {
        model.addAttribute("content", "main");
        return "content/market/marketMain"; // Points to the "content/market/marketMain" template
    }

    @GetMapping("/list")
    public String marketList(PageRequestDTO pageRequestDTO, Model model) {
        long categoryid = 3;
        pageRequestDTO.setCategoryId(categoryid);
        ProductListPageResponseDTO responseDTO =  productService.getProductList(pageRequestDTO);
        log.info("controlllermarket::::"+responseDTO.getProductDTOs());
        model.addAttribute("responseDTO",responseDTO);


        return "content/market/marketList"; // Points to the "content/market/marketList" template
    }

    @GetMapping("/search")
    public String marketSearch(Model model) {
        model.addAttribute("content", "search");
        return "content/market/marketSearch"; // Points to the "content/market/marketSearch" template
    }

    @GetMapping("/view")
    public String marketView(Model model) {
        model.addAttribute("content", "view");



        return "content/market/marketview"; // Points to the "content/market/marketview" template
    }


    @GetMapping("/view/{categoryId}/{productId}")
    public String marketView(@PathVariable long productId,@PathVariable long categoryId,Model model) {
        log.info(productId);
        log.info(categoryId);

       List<ProductCategoryDTO> categoryDTOs =  productCategoryService.selectCategory(categoryId);
       log.info("categories LLLLL "+ categoryDTOs);
       ProductDTO productdto = productService.getProduct(productId);
        log.info("productVIew Controller:::::"+productdto);

        model.addAttribute("categories",categoryDTOs);
        model.addAttribute("products",productdto);

        return "content/market/marketview"; // Points to the "content/market/marketview" template
    }


    @GetMapping("/cart")
    public String marketCart(Model model) {
        model.addAttribute("content", "cart");
        return "content/market/marketcart"; // Points to the "content/market/marketcart" template
    }


    @GetMapping("/order")
    public String marketOrder(Model model) {
        model.addAttribute("content", "order");
        return "content/market/marketorder"; // Points to the "content/market/marketorder" template
    }

    @GetMapping("/completed")
    public String marketOrderCompleted(Model model) {
        model.addAttribute("content", "completed");
        return "content/market/marketorderCompleted"; // Points to the "content/market/marketorderCompleted" template
    }

}
