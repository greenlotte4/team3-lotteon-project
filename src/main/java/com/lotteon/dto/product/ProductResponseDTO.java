package com.lotteon.dto.product;

import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.Product;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProductResponseDTO {
    private Product product;
    private List<OptionDTO> options;
    private ProductDetailsDTO  productDetails;

    @Builder
    public ProductResponseDTO(ProductRequestDTO productRequest) {
        this.product = Product.builder()
                .productName(productRequest.getProductName())
                .categoryId(productRequest.getThirdLevelCategory())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .discount(productRequest.getDiscount())
                .shippingFee(productRequest.getShippingFee())
                .shippingTerms(productRequest.getShippingTerms())
                .point(productRequest.getPoint())
                .sellerId(productRequest.getSellerId())
                .build();
        this.options = new ArrayList<>();
        List<String> optionNames = productRequest.getOptionName();
        List<String> optionDesc = productRequest.getOptionDesc();
        List<Integer> optionStocks = productRequest.getOptionStock();
        for(int i=0; i<optionNames.size(); i++) {
            if(optionNames.get(i)==null){
                break;
            }
            OptionDTO optionDTO = new OptionDTO();
            optionDTO.setOptionName(optionNames.get(i));
            optionDTO.setOptionDesc(optionDesc.get(i));
            optionDTO.setOptionStock(optionStocks.get(i));

            this.options.add(optionDTO);
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
