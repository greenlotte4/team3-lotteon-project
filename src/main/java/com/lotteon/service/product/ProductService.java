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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        Set<OptionDTO> options = insertProduct.getOptions();


        //option insert
        Set<Option> optionSet =  options.stream().map((element) -> modelMapper.map(element, Option.class)).collect(Collectors.toSet());
        for(Option option : optionSet) {
            log.info("option : "+ option);
            optionRepository.save(option);
        }

        product.setOptions(optionSet);

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


    public  ProductPageResponseDTO selectProductsBySellerId(String sellerId,PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("hit",10);


        Page<Product> products=null;
        if(pageRequestDTO.getType() ==null){
           products = productRepository.selectProductBySellerIdForList(sellerId,pageRequestDTO,pageable);
            log.info(products);

        }
        int total = (int) products.getTotalElements();
        log.info("total : "+total);
        List<ProductDTO> productDTOList = products.getContent().stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();

        log.info("productDTOLists : "+productDTOList);

        return ProductPageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .productDTOList(productDTOList)
                .total(total)
                .build();


    }
    public void updateProduct() {}
    public int deleteProduct(Long ProductID) {
        int result=0;
        if(productRepository.existsById(ProductID)) {
            productRepository.deleteById(ProductID);
            result=1;
        }
        return result;

    }
    public void isSaleProduct() {}
}
