package com.lotteon.controller;



import com.lotteon.dto.product.*;
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
    public String productList(Model model, PageRequestDTO pageRequestDTO,Authentication authentication) {

        String user = authentication.getName();
        ProductPageResponseDTO productPageResponseDTO =  productService.selectProductsBySellerId(user,pageRequestDTO);
        model.addAttribute("productPageResponseDTO", productPageResponseDTO);
        log.info(productPageResponseDTO.getProductDTOList());

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


    @GetMapping("/product/delete")
    public String productDelete(@RequestParam("id") long id,Model model, Authentication authentication) {
        String user = authentication.getName();
        log.info(id);
        int result = productService.deleteProduct(id);
        if(result >0){
            //성공시
            return "redirect:/seller/product/list?success=200";
        }

        //실패시
        return "redirect:/seller/product/register?success=100";
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





    @GetMapping("/login")
    public String sellerLogin(Model model) {

        return "content/admin/adminLogin";
    }



}
