package com.lotteon.controller;



import com.lotteon.dto.product.ProductDTO;
import com.lotteon.dto.product.ProductRequestDTO;
import com.lotteon.dto.product.ProductResponseDTO;
import com.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/seller")
public class SellerController {


    private final ProductService productService;

    @GetMapping("/product/list")
    public String productList(Model model,Authentication authentication) {

        String user = authentication.getName();
        List<ProductDTO> productDTOS=  productService.selectProducts();
        model.addAttribute("productDTOS", productDTOS);
        log.info(productDTOS);

        return "content/admin/product/admin_productlist"; // Points to the "content/sellerDynamic" template for product listing
    }

    @GetMapping("/product/register")
    public String productRegister(Model model) {
        return "content/admin/product/admin_productReg"; // Points to the "content/sellerDynamic" template for product registration
    }



    @PostMapping("/product/register")
    public String insertProduct(@ModelAttribute ProductRequestDTO productRequestDTO, Authentication auth, Model model) {
        log.info("전달은 된다.");
        log.info(productRequestDTO);

        //product insert
        ProductResponseDTO responseDTO = new ProductResponseDTO(productRequestDTO);
        log.info("auth ~~~~~~~~~~~~~~~~~~"+auth.getName());
        log.info("responseDTO");

        long result= productService.insertProduct(responseDTO);
        //option insert
        log.info("insertProduct");
        if(result >0){
            return "redirect:/seller/product/list";

        }else{
            return "redirect:/seller/product/register?success=200";
        }
    }

    @GetMapping("/order/delivery")
    public String deliveryStatus(Model model) {
        model.addAttribute("content", "delivery");
        return "content/admin/order/admin_Delivery"; // Points to the "content/sellerDynamic" template for delivery orders
    }

    @GetMapping("/order/status")
    public String orderStatus(Model model) {
        model.addAttribute("content", "status");
        return "content/admin/order/admin_Order"; // Points to the "content/sellerDynamic" template for order status
    }



    @GetMapping("/coupon/list")
    public String couponList(Model model) {
        model.addAttribute("cate", "coupon");
        return "content/admin/coupon/list"; // Points to the "content/sellerDynamic" template for coupon management
    }

    @GetMapping("/coupon/issued")
    public String couponIssued(Model model) {
        model.addAttribute("cate", "coupon");
        model.addAttribute("content", "issued");
        return "content/admin/coupon/issued"; // Points to "content/admin/coupon/issued"
    }

    @GetMapping("/login")
    public String sellerLogin(Model model) {

        return "content/admin/adminLogin";
    }



}
