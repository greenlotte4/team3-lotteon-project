package com.lotteon.service.product;

import com.lotteon.dto.product.OptionDTO;
import com.lotteon.dto.product.ProductDetailsDTO;
import com.lotteon.dto.product.ProductRequestDTO;
import com.lotteon.dto.product.ProductResponseDTO;
import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ProductDetails;
import com.lotteon.repository.product.OptionRepository;
import com.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final ModelMapper modelMapper;
    public Long insertProduct(ProductResponseDTO insertProduct) {


        //product insert
       Product savedProduct= productRepository.save(insertProduct.getProduct());

        List<OptionDTO> options = insertProduct.getOptions();


        for(OptionDTO option: options){
            option.setParentCode(savedProduct.getProductCode());
            option.setParent_id(savedProduct.getProductId());
        }

        //option insert
        List<Option> productOption = options.stream().map(t -> modelMapper.map(t,Option.class)).toList();

        optionRepository.saveAll(productOption);

        ProductDetailsDTO details = insertProduct.getProductDetails();
        details.setProductId(savedProduct.getProductId());



       return savedProduct.getProductId();
    }
    public void selectProduct() {}
    public void selectProducts() {}
    public void updateProduct() {}
    public void deleteProduct() {}
    public void isSaleProduct() {}
}
