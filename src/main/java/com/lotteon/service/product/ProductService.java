package com.lotteon.service.product;

import com.lotteon.dto.product.*;
import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ProductDetails;
import com.lotteon.entity.product.ProductFile;
import com.lotteon.repository.product.OptionRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.service.FileService;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
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


    //seller list
    public  ProductPageResponseDTO selectProductsBySellerId(String sellerId,PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("hit",10);


        Page<Tuple> tuple =null;
        int total = 0;
        if(pageRequestDTO.getType() ==null){
            tuple = productRepository.selectProductBySellerIdForList(sellerId,pageRequestDTO,pageable);

            log.info("여기 Product!!!!!!!!!"+tuple.getContent());
            total = (int) tuple.getTotalElements();

        }


        log.info("total : "+total);
        List<ProductDTO> productDTOList = tuple.stream().map(t->{
            // Tuple에서 Product와 ProductFile 추출
            Product product = t.get(0, Product.class);  // 첫 번째로 조회된 Product
            ProductFile productFile = t.get(1, ProductFile.class);  // 두 번째로 조회된 ProductFile

            // Product를 DTO로 변환
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

            // 추가로 ProductFile 관련 정보를 ProductDTO에 저장하려면 여기에 작성
            productDTO.setFile190(productFile.getSName());  // 예시로 productDTO에 파일 정보 저장

            return productDTO;

        }).collect(Collectors.toList());

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



    //main list
    public ProductListPageResponseDTO selectProductListByCategory(PageRequestDTO pageRequestDTO){
        Pageable pageable = pageRequestDTO.getPageable("hit",10);


        Page<Tuple> tuple =null;
        int total = 0;
        if(pageRequestDTO.getType() ==null){
            tuple = productRepository.selectProductForList(pageRequestDTO,pageable);
            log.info("여기 Product!!!!!!!!!"+tuple.getContent());
            total = (int) tuple.getTotalElements();

        }


        log.info("total : "+total);
        List<ProductListDTO> productDTOList = tuple.stream().map(t->{
            // Tuple에서 Product와 ProductFile 추출
            Product product = t.get(0, Product.class);  // 첫 번째로 조회된 Product

            // Product를 DTO로 변환
            ProductListDTO productDTO = modelMapper.map(product, ProductListDTO.class);

            log.info("productDTO : "+ productDTO);
            // 추가로 ProductFile 관련 정보를 ProductDTO에 저장하려면 여기에 작성
            List<ProductFile> productFiles = product.getFiles();
            List<ProductFileDTO> productFileDTOS = productFiles.stream().map((element) -> modelMapper.map(element, ProductFileDTO.class)).collect(Collectors.toList());
            productDTO.setFiles(productDTO.getProductFiles());
            log.info("productFiles : "+productFiles);

            productDTO.setProductFiles(productFileDTOS);
            return productDTO;

        }).collect(Collectors.toList());

        log.info("productDTOLists : "+productDTOList);
        return ProductListPageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .productDTOList(productDTOList)
                .total(total)
                .build();

    }

    public void isSaleProduct() {}
}
