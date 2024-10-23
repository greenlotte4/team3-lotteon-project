package com.lotteon.service.product;

import com.lotteon.dto.product.*;
import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ProductDetails;
import com.lotteon.entity.product.ProductFile;
import com.lotteon.repository.product.OptionRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final ProductFileService productFileService;

    public Long insertProduct(ProductResponseDTO insertProduct) {

        ProductDTO productDTO = insertProduct.getProduct();
        //product insert
        Product product= modelMapper.map(productDTO, Product.class);

        log.info("Before setting sellerId: " + product.getSellerId());

        product.setSellerId(productDTO.getSellerId());
        log.info("After setting sellerId: " + product.getSellerId());


        //file upload & insert
        List<ProductFileDTO> fileDTOS=  fileService.uploadFile(insertProduct.getImages());
        List<ProductFile> files= new ArrayList<>();
        for(ProductFileDTO productFileDTO : fileDTOS) {
            log.info("produtFileDTO : "+ productFileDTO);
            ProductFile file = productFileService.insertFile(productFileDTO);
            files.add(file);
        }
        product.setFiles(files);

        //option 저장로직
        List<OptionDTO> options = insertProduct.getOptions();


        //option insert
        List<Option> productOption = options.stream().map(t -> modelMapper.map(t,Option.class)).toList();

        optionRepository.saveAll(productOption);
        product.setOptions(productOption);

        ProductDetailsDTO details = insertProduct.getProductDetails();
        ProductDetails productDetails = modelMapper.map(details, ProductDetails.class);
        product.setProductDetails(productDetails);

        Product savedProduct= productRepository.save(product);
        return savedProduct.getProductId();
    }

    //사용자별 productlist
    public  List<ProductDTO> selectProductBySellerId(String sellerId) {
        List<Product> products = productRepository.findBySellerId(sellerId);
        if(products.isEmpty()) {
            return null;
        }
        return products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).toList();
    }

    public void selectProduct() {}
    public  List<ProductDTO> selectProducts() {
        List<Product> products = productRepository.findAll();
        log.info(products);
        return products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
    }
    public void updateProduct() {}
    public void deleteProduct() {}
    public void isSaleProduct() {}
}
