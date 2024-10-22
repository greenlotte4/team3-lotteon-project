package com.lotteon.service.product;

import com.lotteon.dto.product.OptionDTO;
import com.lotteon.dto.product.ProductRequestDTO;
import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.Product;
import com.lotteon.repository.product.OptionRepository;
import com.lotteon.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

    public Long insertProduct(ProductRequestDTO productRequestDTO) {

        Product product = Product.builder()
                .ProductDesc(productRequestDTO.getProductDesc())
                .productName(productRequestDTO.getProductName())
                .price(productRequestDTO.getPrice())
                .point(productRequestDTO.getPoint())
                .stock(productRequestDTO.getStock())
                .discount(productRequestDTO.getDiscount())
                .sellerId(productRequestDTO.getSellerId())
                .shippingFee(productRequestDTO.getShippingFee())
                .shippingTerms(productRequestDTO.getShippingTerms())
                .build();

       Product savedProduct= productRepository.save(product);

        List<Option> options = new ArrayList<>();
        List<String> optionNames = productRequestDTO.getOptionName();
        List<String> optionDesc = productRequestDTO.getOptionDesc();
        List<Integer> optionStocks = productRequestDTO.getOptionStock();

        for(int i=0;i<optionNames.size();i++){
            Option savedOption =  Option.builder()
                    .parentCode(savedProduct.getProductCode())
                    .optionName(optionNames.get(i))
                    .optionDesc(optionDesc.get(i))
                    .optionStock(optionStocks.get(i))
                    .parent_id(savedProduct.getProductId())
                    .build();

            optionRepository.save(savedOption);
        }

       return savedProduct.getProductId();
    }
    public void selectProduct() {}
    public void selectProducts() {}
    public void updateProduct() {}
    public void deleteProduct() {}
    public void isSaleProduct() {}
}
