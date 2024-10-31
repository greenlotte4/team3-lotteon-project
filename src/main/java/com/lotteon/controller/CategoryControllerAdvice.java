package com.lotteon.controller;
/*
    2024.10.30 하진희 캐싱작업시 스케쥴 돌리는기능 업데이트
 */

import com.lotteon.dto.product.ProductCategoryDTO;
import com.lotteon.entity.product.ProductCategory;
import com.lotteon.service.product.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;


@Log4j2
@RequiredArgsConstructor
@ControllerAdvice
public class CategoryControllerAdvice {


   private final ProductCategoryService productCategoryService;
    private final ModelMapper modelMapper;

    @Cacheable("categories")
    @ModelAttribute("categories")
    public List<ProductCategoryDTO> populateCategories() {
        log.info("Fetching category data from the database...");
        List<ProductCategory> categories = productCategoryService.getCategoryHierarchy();
        List<ProductCategoryDTO> categoryDTOs = new ArrayList<>();
        categories.forEach(category -> categoryDTOs.add(convertToDto(category)));
        return categoryDTOs;
    }

    @CacheEvict(value = "categories", allEntries = true)
    public void refreshCategories() {
        log.info("Evicting categories cache...");
    }

    @Scheduled(cron = "0 23 2 * * *") // 매일 오전 10시에 갱신
    public void scheduledUpdate() {
        log.info("Scheduled update triggered at 10 AM.");
        refreshCategories();  // 기존 캐시 비우기
        populateCategories(); // 새로 캐시 채우기
    }

    @CacheEvict(value = "categories", allEntries = true)
    public void updateCategoryData() {
        refreshCategories(); // Evict the cache
        populateCategories(); // Populate the cache externally
    }

    private ProductCategoryDTO convertToDto(ProductCategory category) {
        ProductCategoryDTO categoryDTO = modelMapper.map(category, ProductCategoryDTO.class);
        // 재귀적으로 children도 매핑
        if (category.getChildren() != null) {
            List<ProductCategoryDTO> childrenDTOs = new ArrayList<>();
            for (ProductCategory child : category.getChildren()) {
                childrenDTOs.add(convertToDto(child));
            }
            categoryDTO.setChildren(childrenDTOs);
        }
        return categoryDTO;
    }

}
