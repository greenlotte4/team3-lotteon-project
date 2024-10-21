package com.lotteon.controller;


import com.lotteon.dto.product.ProductCategoryDTO;
import com.lotteon.service.product.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
public class ProductApiController {

    private final ProductCategoryService productCategoryService;


    @GetMapping("/api/categories/level/{level}")
    public List<ProductCategoryDTO> getCategoriesByLevel(@PathVariable int level) {

        log.info("category : "+productCategoryService.getCategoryiesByLevel(level));
        return productCategoryService.getCategoryiesByLevel(level);

    }

    @GetMapping("/api/categories/parent/{parentId}")
    public List<ProductCategoryDTO> getCategoriesByParentId(@PathVariable int parentId) {
        log.info("ParentId : "+productCategoryService.getCategoryiesByLevel(parentId));

        return productCategoryService.getCategoriesByParentId(parentId);

    }




}
