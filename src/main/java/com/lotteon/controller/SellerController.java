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


    @GetMapping("/product/list")
    public String productList(Model model) {
        model.addAttribute("content", "list");
        return "/content/admin/product/admin_productlist"; // Points to the "content/sellerDynamic" template for product listing
    }

    @GetMapping("/product/register")
    public String productRegister(Model model) {
        return "/content/admin/product/admin_productReg"; // Points to the "content/sellerDynamic" template for product registration
    }



    @PostMapping("/product/register")
    public String insertProduct(@ModelAttribute ProductRequestDTO productRequestDTO, Model model) {
        log.info("전달은 된다.");
        log.info(productRequestDTO);


        //product insert

        //option insert
      productService.insertProduct(productRequestDTO);


        return "redirect:/seller/product/list";
    }

    @GetMapping("/order/delivery")
    public String deliveryStatus(Model model) {
        model.addAttribute("content", "delivery");
        return "/content/admin/order/admin_Delivery"; // Points to the "content/sellerDynamic" template for delivery orders
    }

    @GetMapping("/order/status")
    public String orderStatus(Model model) {
        model.addAttribute("content", "status");
        return "/content/admin/order/admin_Order"; // Points to the "content/sellerDynamic" template for order status
    }



    @GetMapping("/coupon/list")
    public String couponList(Model model) {
        model.addAttribute("cate", "coupon");
        return "/content/admin/coupon/list"; // Points to the "content/sellerDynamic" template for coupon management
    }

    @GetMapping("/coupon/issued")
    public String couponIssued(Model model) {
        model.addAttribute("cate", "coupon");
        model.addAttribute("content", "issued");
        return "/content/admin/coupon/issued"; // Points to "content/admin/coupon/issued"
    }



}
