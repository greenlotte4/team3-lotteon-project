package com.lotteon.service;


import com.lotteon.entity.ProdCategory;
import com.lotteon.repository.ProdCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ProdCategoryService {

    @Autowired
    private ProdCategoryRepository prodCategoryRepository;

    public List<ProdCategory> getRootCategories() {
        return prodCategoryRepository.findByParentIsNullOrderById();
    }

    public ProdCategory saveCategory(ProdCategory category) {
        return prodCategoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        prodCategoryRepository.deleteById(id);
    }

    public void updateCategoryOrder(List<ProdCategory> categories) {
        prodCategoryRepository.saveAll(categories);
    }
    public List<ProdCategory> getSubCategories(ProdCategory parent) {
        return prodCategoryRepository.findByParentOrderById(parent);
    }
}
