package com.lotteon.service.product;


import com.lotteon.dto.product.CreateCategoryRequestDTO;
import com.lotteon.dto.product.ProductCategoryDTO;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ProductCategory;
import com.lotteon.repository.Impl.ProductCategoryRepositoryImpl;
import com.lotteon.repository.product.ProductCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    private final ModelMapper modelMapper;
    private final ProductCategoryRepositoryImpl productCategoryRepositoryImpl;

    public ProductCategory insertCategory(CreateCategoryRequestDTO createCategoryRequestDTO  ) {
        ProductCategory parentCategory = null;
        if(createCategoryRequestDTO.getParentId() != null && createCategoryRequestDTO.getLevel() > 1) {
            parentCategory = productCategoryRepository.findById(createCategoryRequestDTO.getParentId())
                    .orElseThrow(()->new EntityNotFoundException("parent category not found"));

        }



        // Create new category
        ProductCategory newCategory = ProductCategory.builder()
                .name(createCategoryRequestDTO.getName())
                .parent(parentCategory)  // Set parent category if applicable
                .level(createCategoryRequestDTO.getLevel())
                .subcategory(createCategoryRequestDTO.getSubcategory())
                .disp_yn(createCategoryRequestDTO.getDispYn())
                .note(createCategoryRequestDTO.getNote())
                .build();

        // Save the new category
        return productCategoryRepository.save(newCategory);
    }

    public void updateCategory(){}
    public void deleteCategory(){}


//    level로 카테고리 가져오기
    public List<ProductCategoryDTO> getCategoriesByLevel(int level){

        List<ProductCategory> categories = productCategoryRepository.findByLevel(level);
        return categories.stream()
                .map(category -> modelMapper.map(category, ProductCategoryDTO.class))
                .collect(Collectors.toList());  // Use collect for Java 8+
    }
// parentId값을 기준으로 카테고리 가져오기
    public List<ProductCategoryDTO> getCategoriesByParentId(long parentId){

        List<ProductCategory> categories =  productCategoryRepository.findByParentId(parentId);

        if(categories.isEmpty()){
            log.warn("No categories found for parent id {}", parentId);
        }
        return categories.stream()
                .map(category -> modelMapper.map(category, ProductCategoryDTO.class))
                .toList();
    }


    //메인 카테고리 가져오기
    public ProductCategoryDTO getCategoryById(long id){
        Optional<ProductCategory> opt  = productCategoryRepository.findById(id);
        if (opt.isPresent()) {
            return modelMapper.map(opt.get(), ProductCategoryDTO.class);
        }
        return null;

    }

    public List<ProductCategoryDTO> selectCategory(long id){

       List<ProductCategory> categories =  productCategoryRepository.SelectParentCategories(id);

       List<ProductCategoryDTO> categoriesDTO = categories.stream().map(category -> modelMapper.map(category, ProductCategoryDTO.class)).toList();

        return categoriesDTO;
    }

    public List<ProductCategory> getCategoryHierarchy() {
        return buildCategoryTree(null); // 최상위 카테고리부터 시작 (parentId가 NULL인 것)
    }

    private List<ProductCategory> buildCategoryTree(Long parentId) {
        List<ProductCategory> categories = productCategoryRepository.findByParentId(parentId);
        for (ProductCategory category : categories) {
            category.setChildren(buildCategoryTree(category.getId())); // 재귀적으로 하위 카테고리를 가져옴
        }


        return categories;
    }
}
