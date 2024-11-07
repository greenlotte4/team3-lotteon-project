package com.lotteon.controller;


import com.lotteon.dto.product.PageRequestDTO;
import com.lotteon.dto.product.ProductCategoryDTO;
import com.lotteon.dto.product.ProductListPageResponseDTO;
import com.lotteon.entity.product.ProductCategory;
import com.lotteon.service.product.ProductCategoryService;
import com.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
public class ProductApiController {

    private final ProductCategoryService productCategoryService;
    private final ProductService productService;

    @GetMapping("/api/categories")
    public List<ProductCategory> getCategories() {
        return productCategoryService.getCategoryHierarchy();
    }

    @GetMapping("/api/categories/level/{level}")
    public List<ProductCategoryDTO> getCategoriesByLevel(@PathVariable int level) {

        log.info("category : "+productCategoryService.getCategoriesByLevel(level));
        return productCategoryService.getCategoriesByLevel(level);

    }

    @GetMapping("/api/categories/parent/{parentId}")
    public List<ProductCategoryDTO> getCategoriesByParentId(@PathVariable long parentId) {
        log.info("ParentId : "+productCategoryService.getCategoriesByParentId(parentId));

        return productCategoryService.getCategoriesByParentId(parentId);

    }




    @GetMapping("/product/list/ajax")
    public ResponseEntity<ProductListPageResponseDTO> productList(Model model, PageRequestDTO pageRequestDTO, Authentication authentication,
                                                                  @RequestParam(value="pg",required=false,defaultValue = "1") int pg,
                                                                  @RequestParam(value = "type",required = false) String type,
                                                                  @RequestParam(value = "keyword",required = false) String keyword) {
        log.info("일단 여기!!!");
        String user = authentication.getName();
        String role = authentication.getAuthorities().toString();
        log.info("rolE!!!!!!!!!"+role);
        log.info("type, keyword : "+type+keyword);
        pageRequestDTO.setPage(pg);
        pageRequestDTO.setType(type);
        pageRequestDTO.setKeyword(keyword);

        ProductListPageResponseDTO productPageResponseDTO =new ProductListPageResponseDTO();
        if(role.contains("ROLE_ADMIN")) {

            productPageResponseDTO = productService.selectProductAll(pageRequestDTO);
            log.info("확인!!!!!!!!!!!!!!!!!!ADMIN"+productPageResponseDTO);


            log.info("ROLE!!!! : "+productPageResponseDTO);
        }else if(role.contains("ROLE_SELLER")){
            productPageResponseDTO = productService.selectProductBySellerId(user, pageRequestDTO);
            log.info("ROLE_SELLER!!!! : "+productPageResponseDTO);
            log.info("확인!!!!!!!!!!!!!!!!!!"+productPageResponseDTO.getKeyword());

        }

        model.addAttribute("productPageResponseDTO", productPageResponseDTO);
        model.addAttribute("productList", "productList");

        return ResponseEntity.ok(productPageResponseDTO);
    }




}
