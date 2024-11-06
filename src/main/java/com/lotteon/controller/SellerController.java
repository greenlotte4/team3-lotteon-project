package com.lotteon.controller;



import com.lotteon.dto.product.*;
import com.lotteon.service.product.ProductCategoryService;
import com.lotteon.service.user.UserService;
import com.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/seller")
public class SellerController {


    private final ProductService productService;
    private final AuthenticationManager authenticationManager; // AuthenticationManager로 수정
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ProductCategoryService productCategoryService;


    @GetMapping("/product/list")
    public String productList(Model model, PageRequestDTO pageRequestDTO, Authentication authentication) {

        String user = authentication.getName();
        ProductListPageResponseDTO productPageResponseDTO = productService.selectProductBySellerId(user, pageRequestDTO);
        model.addAttribute("productPageResponseDTO", productPageResponseDTO);
        model.addAttribute("productList", "productList");

        return "content/admin/product/admin_productlist"; // Points to the "content/sellerDynamic" template for product listing
    }

    @GetMapping("/product/register")
    public String productRegister(Model model) {
        return "content/admin/product/admin_productReg"; // Points to the "content/sellerDynamic" template for product registration
    }


    @GetMapping("/product/modify")
    public String productModify(@RequestParam long id, Model model) {

        ProductDTO productdto = productService.getProduct(id);
        log.info("여기야여기!!!!!!"+productdto);
        List<ProductCategoryDTO> categoryDTOs =  productCategoryService.getAllParentCategoryDTOs(productdto.getCategoryId());
        log.info("dsdsdsdsd"+categoryDTOs);

        model.addAttribute("productDTO", productdto);
        model.addAttribute("categoryDTOs", categoryDTOs);
        return "content/admin/product/admin_productmodify"; // Points to the "content/sellerDynamic" template for product registration
    }


    @PostMapping("/product/register")
    public String insertProduct(@ModelAttribute ProductRequestDTO productRequestDTO, Authentication auth, Model model) {
        log.info("전달은 된다.");
        log.info(productRequestDTO);

        //product insert
        ProductResponseDTO responseDTO = new ProductResponseDTO(productRequestDTO);
        log.info("auth ~~~~~~~~~~~~~~~~~~" + auth.getName());
        log.info("responseDTO");

        long result = productService.insertProduct(responseDTO);
        //option insert
        log.info("insertProduct");
        if (result > 0) {
            return "redirect:/seller/product/list";

        } else {
            return "redirect:/seller/product/register?success=200";
        }
    }


    @GetMapping("/product/delete")
    public String productDelete(@RequestParam("id") long id, Model model, Authentication authentication) {
        String user = authentication.getName();
        log.info(id);
        int result = productService.deleteProduct(id);
        if (result > 0) {
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


    @PostMapping("/login")
    public String login(@RequestParam("inId") String username,
                        @RequestParam("password") String password,
                        Model model) {
                try {
                    if (userService.sellerlogin(username, password)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(username, password);
                        Authentication authentication = authenticationManager.authenticate(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // 로그인 성공 시 Member의 name 값을 가져와 모델에 추가
                        String memberName = userService.getMemberNameByUsername(username);
                        model.addAttribute("memberName", memberName);

                        return "redirect:/"; // 로그인 성공 후 이동할 페이지
                    } else {
                        return "redirect:/user/term?type=seller";
                    }

                } catch (Exception e) {
                    log.error("로그인 실패: ", e);
                    model.addAttribute("error", "로그인 실패: 아이디 또는 비밀번호가 잘못되었습니다."); // 오류 메시지 추가
                    return "content/admin/adminLogin"; // 로그인 페이지로 돌아가기
                }
    }
}

