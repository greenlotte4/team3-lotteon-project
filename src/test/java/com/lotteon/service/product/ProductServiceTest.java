package com.lotteon.service.product;

import com.lotteon.dto.product.PageRequestDTO;
import com.lotteon.dto.product.ProductPageResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;


    @Test
    void selectProductsBySellerId() {
        String sellerId= "admin123";
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        Pageable pageable = pageRequestDTO.getPageable("hit",10);
        ProductPageResponseDTO result = productService.selectProductsBySellerId(sellerId, pageRequestDTO);


        assertNotNull(result);
        assertTrue(result.getTotal()>0);
    }
}