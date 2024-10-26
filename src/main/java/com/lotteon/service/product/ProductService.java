package com.lotteon.service.product;

/*
    날짜 : 2024.10.20
    이름 : 하진희
    내용 : product Insert

   ==========================
   추가작업
   2024.10.23 하진희 - product list service 추가
   2024.10.26 하진희 - product insert부분 수정 ( image sname 넣기)
 */


import com.lotteon.dto.User.SellerDTO;
import com.lotteon.dto.product.*;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.product.Option;
import com.lotteon.entity.product.Product;
import com.lotteon.entity.product.ProductDetails;
import com.lotteon.entity.product.ProductFile;
import com.lotteon.repository.product.OptionRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.repository.user.SellerRepository;
import com.lotteon.service.FileService;
import com.lotteon.service.user.SellerService;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final SellerRepository sellerRepository;
    private final SellerService sellerService;

    public Long insertProduct(ProductResponseDTO insertProduct) {

        ProductDTO productDTO = insertProduct.getProduct();
        //product insert



       Optional<Seller> opt = sellerRepository.findByUserUid(productDTO.getSellerId());
        log.info("seller opt /////////"+opt);
        long sellerNo=0;
        if(opt.isPresent()) {
            Seller seller = opt.get();
            log.info("seller seller /////////"+seller);

            sellerNo = seller.getId();
        }
        productDTO.setSellerNo(sellerNo);

        Product product= modelMapper.map(productDTO, Product.class);

        //file upload & insert
        List<ProductFileDTO> fileDTOS=  fileService.uploadFile(insertProduct.getImages());
        List<ProductFile> files= new ArrayList<>();
        for(ProductFileDTO productFileDTO : fileDTOS) {
            log.info("produtFileDTO : "+ productFileDTO);
            ProductFile file = productFileService.insertFile(productFileDTO);
            files.add(file);
        }
        log.info("filessssssssssssssssss:"+files);
        product.setFiles(files);

        //option 저장로직
        List<OptionDTO> options = insertProduct.getOptions();


        //option insert
        List<Option> savedOptions = new ArrayList<Option>();
        for(OptionDTO option : options) {
            log.info("option : "+ option);
            Option toEntity = modelMapper.map(option, Option.class);
           Option savedOption =  optionRepository.save(toEntity);
           savedOptions.add(savedOption);
        }

        product.setOptions(savedOptions);

        ProductDetailsDTO details = insertProduct.getProductDetails();
        ProductDetails productDetails = modelMapper.map(details, ProductDetails.class);
        product.setProductDetails(productDetails);

        Product savedProduct= productRepository.save(product);
        return savedProduct.getProductId();
    }

    //사용자별 productlist
    public ProductListPageResponseDTO selectProductBySellerId(String sellerId,PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("sold",10);
        Page<Product> products = productRepository.findBySellerId(sellerId,pageable);
        if(products.isEmpty()) {
            return ProductListPageResponseDTO.builder()
                    .productDTOs(Collections.emptyList())
                    .pageRequestDTO(pageRequestDTO)
                    .total(0)
                    .build();
        }
       List<ProductDTO> productDTOs =  products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

        return ProductListPageResponseDTO.builder()
                .total(productDTOs.size())
                .productDTOs(productDTOs)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    public void selectProduct() {}
    public  List<ProductDTO> selectProducts() {
        List<Product> products = productRepository.findAll();
        log.info(products);
        return products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
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
    public ProductListPageResponseDTO getProductList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("hit",10);

        Page<Tuple> tuples = productRepository.selectProductByCategory(pageRequestDTO,pageable);
        log.info("disldksldkfsd: "+tuples.getContent().toString());

        if(tuples.isEmpty()) {
            return ProductListPageResponseDTO.builder()
                    .productDTOs(Collections.emptyList())
                    .pageRequestDTO(pageRequestDTO)
                    .total(0)
                    .build();
        }

        List<ProductDTO> productDTOs = tuples.stream().map(tuple -> {
            Product product = tuple.get(0,Product.class);  // qProduct 엔티티 객체를 가져옴
            Seller seller = tuple.get(1,Seller.class);    // qSeller 엔티티 객체를 가져옴

            // `ProductDTO`와 `SellerDTO`로 매핑
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            SellerDTO sellerDTO = modelMapper.map(seller, SellerDTO.class);

            // `ProductDTO`에 `SellerDTO`를 설정 (ProductDTO가 SellerDTO 필드를 가정)
            productDTO.setCompany(sellerDTO.getCompany());

            return productDTO;
        }).collect(Collectors.toList());

        return ProductListPageResponseDTO.builder()
                .total((int) tuples.getTotalElements())
                .productDTOs(productDTOs)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

//////////////////////////////////////////////////////////
//
    //view page select
    public ProductDTO getProduct(Long ProductID) {

        Product product = productRepository.findByProductId(ProductID);
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        List<OptionDTO> optionDTOs = product.getOptions().stream().map(option -> modelMapper.map(option, OptionDTO.class)).collect(Collectors.toList());
        List<ProductFileDTO> productFileDTOs = product.getFiles().stream().map(file -> modelMapper.map(file, ProductFileDTO.class)).collect(Collectors.toList());
        productDTO.setOptions(optionDTOs);
        productDTO.setProductFiles(productFileDTOs);
        List<String> filedesc= new ArrayList<>();
        if(!productFileDTOs.isEmpty()){
            for(ProductFileDTO productFileDTO : productFileDTOs){
                if(productFileDTO.getType().equals("940")){
                    filedesc.add(productFileDTO.getSName());
                }
            }
        }
        productDTO.setFiledesc(filedesc);


        SellerDTO sellerDTO = sellerService.getSeller(product.getSellerId());
        productDTO.setSeller(sellerDTO);

        log.info("view!!!!!!!!!!!!!!!!!!1; "+productDTO);


        return productDTO;
    }


        public void isSaleProduct() {}
}
