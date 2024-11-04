package com.lotteon.controller;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotteon.dto.product.*;
import com.lotteon.entity.product.Product;
import com.lotteon.service.user.UserService;
import com.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.groovy.util.Maps;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/seller")
public class SellerController {


    private final ProductService productService;
    private final AuthenticationManager authenticationManager; // AuthenticationManager로 수정
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();



    @GetMapping("/product/list")
    public String productList(Model model, PageRequestDTO pageRequestDTO, Authentication authentication) {

        String user = authentication.getName();
        String role = authentication.getAuthorities().toString();
        ProductListPageResponseDTO productPageResponseDTO = null;
        if(role.contains("ROLE_ADMIN")) {
            productPageResponseDTO = productService.selectProductAll(pageRequestDTO);
        }else if(role.contains("ROLE_SELLER")){
            productPageResponseDTO = productService.selectProductBySellerId(user, pageRequestDTO);
        }
         
        model.addAttribute("productPageResponseDTO", productPageResponseDTO);
        model.addAttribute("productList", "productList");

        return "content/admin/product/admin_productlist"; // Points to the "content/sellerDynamic" template for product listing
    }

    @GetMapping("/product/register")
    public String productRegister(Model model) {
        return "content/admin/product/admin_productReg"; // Points to the "content/sellerDynamic" template for product registration
    }

    @ResponseBody
    @PostMapping(value = "/product/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Long>> registerProduct(
            @RequestPart("productData") String productJson,
            @RequestParam("files") List<MultipartFile> files) {
        long result=0;
        Map<String, Long> response = new HashMap<>();

        try{
            ObjectMapper objectMapper = new ObjectMapper();

            // Deserialize JSON strings into DTOs or lists
            ProductRequestDTO productRequestDTO = objectMapper.readValue(productJson, ProductRequestDTO.class);
            System.out.println("Received product data: " + productRequestDTO);
            System.out.println("Received options: " + productRequestDTO.getOptions());
            System.out.println("Received combinations: " + productRequestDTO.getCombinations());


            ProductResponseDTO responseDTO = new ProductResponseDTO(productRequestDTO,files);
            result = productService.insertProduct(responseDTO);
            response.put("result", result);

            // Save product data if necessary
            // productService.saveProduct(productRequestDTO);
            // Return a success response

        }catch (Exception e){
            e.printStackTrace();
            response.put("result", result);
            return ResponseEntity.ok().body(response);
        }

        return ResponseEntity.ok().body(response);



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


    @PostMapping("/product/deleteSelected")
    @ResponseBody  // Ensures JSON response
    public Map<String, Object> deleteSelectedProducts(@RequestBody List<Long> selectedProducts, Authentication authentication) {
        log.info("Received product IDs: {}", selectedProducts);

        Map<String, Object> response = new HashMap<>();
        try {
            int result = productService.deleteProducts(selectedProducts);
            response.put("success", result == selectedProducts.size());
            response.put("message", result == selectedProducts.size() ? "Selected products deleted successfully." : "Some products could not be deleted.");
        } catch (Exception e) {
            log.error("Error deleting products", e);
            response.put("success", false);
            response.put("message", "An error occurred while deleting products.");
        }
        return response;
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

