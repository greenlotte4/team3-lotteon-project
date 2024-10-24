package com.lotteon.dto.product;


import com.lotteon.dto.User.SellerDTO;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ProductFile;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductWithDTO {

    private ProductDTO productdto;
    private List<ProductFileDTO> files;
    private String Company;
    private String seller;

    public ProductWithDTO(ProductDTO productDTO,List<ProductFileDTO> files) {
        this.productdto = productDTO;
        this.files = files;

    }


}
