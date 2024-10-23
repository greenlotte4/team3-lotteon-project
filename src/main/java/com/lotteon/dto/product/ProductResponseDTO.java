package com.lotteon.dto.product;

import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.Product;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class ProductResponseDTO{
    private ProductDTO product;
    private Set<OptionDTO> options;
    private ProductDetailsDTO productDetails;
    private MultiValueMap<String, MultipartFile> images;


    @Builder
    public ProductResponseDTO(ProductRequestDTO productRequest) {
        log.info("여기 ");
        this.product = ProductDTO.builder()
                .categoryId(productRequest.getThirdLevelCategory())
                .ProductDesc(productRequest.getProductDesc())
                .productName(productRequest.getProductName())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .discount(productRequest.getDiscount())
                .shippingFee(productRequest.getShippingFee())
                .shippingTerms(productRequest.getShippingTerms())
                .point(productRequest.getPoint())
                .sellerId(productRequest.getSellerId())
                .build();

        log.info("여기2");
        this.options= new LinkedHashSet<>();
        List<String> optionNames = productRequest.getOptionName();
        List<String> optionDesc = productRequest.getOptionDesc();
        List<Integer> optionStocks = productRequest.getOptionStock();
        for(int i=0; i<optionNames.size(); i++) {
            if(optionNames.get(i)==null){
                continue;
            }
            OptionDTO optionDTO = new OptionDTO();
            optionDTO.setOptionName(optionNames.get(i));
            optionDTO.setOptionDesc(optionDesc.get(i));
            optionDTO.setOptionStock(optionStocks.get(i));

            this.options.add(optionDTO);
        }
        if (productRequest.getFiles() != null) {
            List<MultipartFile> files = productRequest.getFiles();
            int size = files.size();
            if(size !=0 ){
                this.images = new LinkedMultiValueMap<>();
                for(int i=0; i<size; i++) {
                    MultipartFile file = files.get(i);
                    if (i == 0) {
                        images.add("190", file);
                    } else if (i == 1) {
                        images.add("230", file);
                    } else if (i == 2) {
                        images.add("456", file);
                    } else {
                        images.add("940", file);
                    }
                }
            }
        }


        this.productDetails = ProductDetailsDTO.builder()
                .condition(productRequest.getCondition())
                .tax(productRequest.getTax())
                .receiptIssuance(productRequest.getReceiptIssuance())
                .manufactureCountry(productRequest.getManufactureCountry())
                .busniesstype(productRequest.getBusniesstype())
                .build();



    }


}
