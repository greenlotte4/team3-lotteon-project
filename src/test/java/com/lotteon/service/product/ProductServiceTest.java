package com.lotteon.service.product;

import com.lotteon.dto.product.ProductRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void insertProduct() {
        List<String> optionNames = new ArrayList<>();
        optionNames.add("name4");
        optionNames.add("description4");
        optionNames.add("price4");
        List<String> optionDesc =  new ArrayList<>();
        optionDesc.add("description4");
        optionDesc.add("price4");
        optionDesc.add("name4");
        List<Integer> optionStocks =  new ArrayList<>();
        optionStocks.add(1);
        optionStocks.add(2);
        optionStocks.add(3);

        ProductRequestDTO productRequestDTO = ProductRequestDTO.builder()
                .productName("test4")
                .optionDesc(optionDesc)
                .optionName(optionNames)
                .optionStock(optionStocks)
                .price(2000)
                .stock(20)
                .shippingFee(200)
                .shippingTerms(2000)
                .point(2)
                .sellerId("seller")
                .discount(0)
                .productDesc("test")
                .rdate(String.valueOf(LocalDateTime.now()))
                .busniesstype("1")
                .condition("새상품")
                .build();

        Long savedProductId = productService.insertProduct(productRequestDTO);

        System.out.println("savedProductId : "+savedProductId);

    }

    @Test
    void selectProduct() {
    }

    @Test
    void selectProducts() {
    }

    @Test
    void updateProduct() {
    }

    @Test
    void deleteProduct() {
    }

    @Test
    void isSaleProduct() {
    }
}