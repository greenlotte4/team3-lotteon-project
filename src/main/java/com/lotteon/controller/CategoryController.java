//package com.lotteon.controller;
//
//import com.lotteon.entity.ProdCategory;
//import com.lotteon.service.ProdCategoryService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@Log4j2
//@RequestMapping("/api/categories")
//public class CategoryController {
//
//    private final ProdCategoryService prodCategoryService;
//
//
//    @GetMapping
//    public List<ProdCategory> getCategories() {
//        return prodCategoryService.getRootCategories();
//    }
//
//    @PostMapping("/save")
//    public void saveCategory(@RequestBody ProdCategory category) {
//        prodCategoryService.saveCategory(category);
//    }
//
//    // Optional: if you want to load subcategories dynamically
//    @GetMapping("/{parentId}/subcategories")
//    public List<ProdCategory> getSubCategories(@PathVariable Long parentId) {
//        ProdCategory parent = new ProdCategory();
//        parent.setId(parentId);
//        return prodCategoryService.getSubCategories(parent);
//    }
//    @PostMapping
//    public ResponseEntity<ProdCategory> addCategory(@RequestBody ProdCategory category) {
//        ProdCategory savedCategory = prodCategoryService.saveCategory(category);
//        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
//    }
//
//}
