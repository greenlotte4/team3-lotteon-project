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
import com.lotteon.dto.product.request.ProductViewResponseDTO;
import com.lotteon.entity.User.Seller;
import com.lotteon.entity.product.*;
import com.lotteon.repository.ReviewFileRepository;
import com.lotteon.repository.ReviewRepository;
import com.lotteon.repository.product.OptionRepository;
import com.lotteon.repository.product.ProductCategoryRepository;
import com.lotteon.repository.product.ProductRepository;
import com.lotteon.repository.user.SellerRepository;
import com.lotteon.service.FileService;
import com.lotteon.service.ReviewService;
import com.lotteon.service.user.SellerService;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final ReviewFileRepository reviewFileRepository;
    private final ProductCategoryService productCategoryService;
    private final ProductCategoryRepository productCategoryRepository;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    public void updatehit(Long productId){
       Optional<Product> opt =  productRepository.findByProductId(productId);
       if(opt.isPresent()){
           Product product = opt.get();
           product.setHit();
           productRepository.save(product);
       }


    }

    @Transactional
    public int deleteProducts(List<Long> productIds) {
        log.info("delete들어왔다");
//        List<Product> products = productRepository.findAllById(productIds);
//        for (Product product : products) {
//            try {
//                // Assuming each product has a method to get its associated file paths
//                List<ProductFile> files = product.getFiles(); // e.g., file190 path or other file paths
//                for(ProductFile file : files){
//                  String filePath = file.getPath();
//                  Files.delete(Paths.get(file.getPath()));
//                }
//
//            } catch (Exception e) {
//                // Log and handle any exceptions, like files not found or permission issues
//                log.error("Error deleting file for product {}: {}", product.getProductId(), e.getMessage());
//            }
//        }
        int result = 0;
        for(Long productId : productIds){

            Optional<Product> product = productRepository.findByProductId(productId);
            if(product.isPresent()){
                log.info("product!!!!!"+product);
                productRepository.deleteById(productId);
                result++;
            }

        }


        log.info("result가 있나?????:"+result);
        return result;
    }

    @Transactional
    public Long insertProduct(ProductResponseDTO insertProduct) {
        log.info("insert들어왔다!!!!");
        ProductDTO productDTO = insertProduct.getProduct();
        SellerDTO seller = sellerService.getSeller(productDTO.getSellerId());
        productDTO.setSellerNo(seller.getId());
        productDTO.setHit(0L);
        productDTO.setPoint(0);
        productDTO.setSold(0L);
        Product product= modelMapper.map(productDTO, Product.class);
        Optional<ProductCategory> opt = productCategoryRepository.findById(product.getCategoryId());
        ProductCategory productCategory=null;
        if(opt.isPresent()){
            log.info("22222222222!!!!");

            productCategory= opt.get();
            //file upload & insert
            List<ProductFileDTO> fileDTOS=  fileService.uploadFile(insertProduct.getImages(),productCategory);
            Set<ProductFile> files= new HashSet<>();
            for(ProductFileDTO productFileDTO : fileDTOS) {
                log.info("produtFileDTO : "+ productFileDTO);
                ProductFile file = productFileService.insertFile(productFileDTO);
                files.add(file);
            }
            log.info("filessssssssssssssssss:"+files);
            product.setFiles(files);
        }




        // OptionGroup 및 Option 저장 로직
        List<OptionGroupDTO> optionGroupDTOS = insertProduct.getOptionGroups();


        //OptionGroup insert
        Set<OptionGroup> savedOptions = new HashSet<>();
        log.info("333333333333!!!!");

        for(OptionGroupDTO optionGroupDTO : optionGroupDTOS) {

            OptionGroup optionGroup = modelMapper.map(optionGroupDTO, OptionGroup.class);
            optionGroup.setProduct(product);  // Product와 연관 설정
            List<OptionItem> optionItems = new ArrayList<>();
            List<OptionItemDTO> optionItemDTOS =  optionGroupDTO.getOptionItems();
            for(OptionItemDTO optionItemDTO : optionItemDTOS) {
                OptionItem optionItem = modelMapper.map(optionItemDTO, OptionItem.class);
                optionItem.setOptionGroup(optionGroup);
                optionItems.add(optionItem);
            }
            optionGroup.setOptionItems(optionItems);
            savedOptions.add(optionGroup);

        }

        product.setOptionGroups(savedOptions);
        log.info("44444444444444!!!!");


        // ProductOptionCombination 설정
        Set<ProductOptionCombination> combinations = new HashSet<>();
        for (ProductOptionCombinationDTO combinationDTO :  insertProduct.getProductOptionCombinations()) {
            ProductOptionCombination combination = modelMapper.map(combinationDTO, ProductOptionCombination.class);
            combination.setProduct(product); // Product와 연관 설정
            combinations.add(combination);
        }

        // Product에 OptionCombinations 설정
        product.setOptionCombinations(combinations);
        log.info("55555555555555!!!!");

        ProductDetailsDTO details = insertProduct.getProductDetails();
        ProductDetails productDetails = modelMapper.map(details, ProductDetails.class);
        product.setProductDetails(productDetails);

        Product savedProduct= productRepository.save(product);
        return savedProduct.getProductId();
    }

    //사용자별 productlist
    public ProductListPageResponseDTO selectProductBySellerId(String sellerId,PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("sold",10);
        Page<Product> products;
        if(pageRequestDTO.getType()==null || pageRequestDTO.getKeyword()==null) {
            products = productRepository.findBySellerId(sellerId, pageable);
            if (products.isEmpty()) {
                return ProductListPageResponseDTO.builder()
                        .pageRequestDTO(pageRequestDTO)
                        .total(0)
                        .build();
            }
        }else{
            String type=pageRequestDTO.getType();
            String keyword=pageRequestDTO.getKeyword();
            switch (type) {
                case "productName":
                    products= productRepository.findByProductNameContainingAndSellerId(keyword,sellerId,pageable);
                    break;
                case "productCode":
                    products= productRepository.findByProductCodeContainingAndSellerId(keyword,sellerId,pageable);
                    break;
                case "seller":
                    products= productRepository.findBySellerIdContainingAndSellerId(keyword,sellerId,pageable);
                    break;
                case "manufacturer":
                    products= productRepository.findByProductDetailsContainingAndSellerId(keyword,sellerId,pageable);
                    break;
                default:
                    return null;
            }
        }
       List<ProductDTO> productDTOs =  products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

        return ProductListPageResponseDTO.builder()
                .total(productDTOs.size())
                .ProductDTOs(productDTOs)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    public ProductListPageResponseDTO selectProductAll(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable("hit",10);
        Page<Product> products;
        if(pageRequestDTO.getType()==null || pageRequestDTO.getKeyword()==null){
            products = productRepository.findAll(pageable);
            if(products.isEmpty()) {
                return ProductListPageResponseDTO.builder()
                        .pageRequestDTO(pageRequestDTO)
                        .total(0)
                        .build();
            }
        }else{
            String type=pageRequestDTO.getType();
            String keyword=pageRequestDTO.getKeyword();
            switch (type) {
                case "productName":
                   products= productRepository.findByProductNameContaining(keyword,pageable);
                   break;
                case "productCode":
                    products= productRepository.findByProductCodeContaining(keyword,pageable);
                    break;
                case "seller":
                    products= productRepository.findBySellerIdContaining(keyword,pageable);
                    break;
                case "manufacturer":
                    products= productRepository.findByProductDetailsContaining(keyword,pageable);
                    break;
                default: return  ProductListPageResponseDTO.builder()
                        .pageRequestDTO(pageRequestDTO)
                        .total(0)
                        .build();
            }
        }



        List<ProductDTO> productDTOs =  products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());

        return ProductListPageResponseDTO.builder()
                .total(productDTOs.size())
                .ProductDTOs(productDTOs)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    public ProductDTO selectProduct(long productId) {
        Optional<Product> optionalProduct = productRepository.findByProductId(productId);
        Product product = null;
        if(optionalProduct.isPresent()){
            product = optionalProduct.get();
        }
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        SellerDTO sellerDTO = sellerService.getSeller(product.getSellerId());
        productDTO.setSeller(sellerDTO);

        log.info("변경해도 있을까?????????ㅣ:"+productDTO.getSeller());

        return productDTO;

    }
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
//    @Cacheable(value = "productListCache", key = "#pageRequestDTO.categoryId + '-' + #sort + '-' + #pageRequestDTO.page")
    public ProductListPageResponseDTO getSortProductList(PageRequestDTO pageRequestDTO,String sort ) {
       log.info("일단 들어와"+pageRequestDTO);
        Pageable pageable = pageRequestDTO.getPageable(sort,10);

        Page<ProductSummaryDTO> tuples = productRepository.selectProductByCategory(pageRequestDTO,pageable,sort);

        log.info("PageRequestDTO: " + pageRequestDTO);
        log.info("Pageable: " + pageable.getPageSize());
        log.info("Tuples result: " + tuples.getContent());
        log.info("disldksldkfsd: "+tuples.getContent().toString());

        if(tuples.isEmpty()) {
            log.info("여기는아니잖아");
            return ProductListPageResponseDTO.builder()
                    .productSummaryDTOs(tuples.getContent())
                    .pageRequestDTO(pageRequestDTO)
                    .total(0)
                    .build();
        }
        try{
            log.info("여기깢!!");

            List<ProductSummaryDTO> productListWithReviews = tuples.getContent().stream()
                    .map(product -> {
                        int reviewCount = reviewRepository.countByProduct_ProductId(product.getProductId()); // 리뷰 갯수 조회
                        product.setReviewCount(reviewCount); // 리뷰 갯수 설정
                        return product;
                    })
                    .collect(Collectors.toList());

            ProductListPageResponseDTO list=  ProductListPageResponseDTO.builder()
                    .total((int) tuples.getTotalElements())
                    .productSummaryDTOs(tuples.getContent())
                    .pageRequestDTO(pageRequestDTO)
                    .build();

            log.info("List!!!!"+list);
            return list;

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
//////////////////////////////////////////////////////////
//


    public ProductDTO selectByProductId(Long productId) {
        Product product = productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return product.toDTO(product); // Convert to DTO


    }


    //view page select
    public ProductDTO getProduct(Long ProductID) {

        Optional<Product> opt = productRepository.findByProductId(ProductID);
        Product product = null;
        if (opt.isPresent()) {
            product = opt.get();
        }

        log.info("여기!!!1");
        if (product == null) {
            throw new ProductNotFoundException("Product with ID " + ProductID + " not found");
        }
        log.info("여기!!!2");

        ProductDTO productDTO = product.toDTO(product);
        log.info("여기!!!333"+productDTO);
        List<Option> options = product.getOptions();
        log.info("option!!!!!!:"+options);
        List<OptionDTO> optionDTOS = new ArrayList<>();
        if(!options.isEmpty() || options != null){
            optionDTOS = product.getOptions().stream().map(option -> modelMapper.map(option, OptionDTO.class)).collect(Collectors.toList());
        }
        productDTO.setOptions(optionDTOS);


        List<OptionGroupDTO> optionGroupDTOS = product.getOptionGroups().stream().map(
                optionGroup -> {
                    List<OptionItem> optionItems =  optionGroup.getOptionItems();

                    OptionGroupDTO optionGroupDTO = modelMapper.map(optionGroup,OptionGroupDTO.class);
                    optionGroupDTO.setOptionItems(optionItems.stream().map(t-> modelMapper.map(t,OptionItemDTO.class)).collect(Collectors.toList()));
                    return optionGroupDTO;
                }).sorted(Comparator.comparing(OptionGroupDTO::getOptionGroupId))
                .collect(Collectors.toList());
        productDTO.setOptionGroups(optionGroupDTOS);
        log.info("여기3!!"+optionGroupDTOS);

        // Map Product Option Combinations
        Set<ProductOptionCombination> productOptionCombinations =product.getOptionCombinations();
        Set<ProductOptionCombinationDTO> productOptionCombinationDTOS = new HashSet<>();
        for (ProductOptionCombination productOptionCombination : productOptionCombinations) {
            ProductOptionCombinationDTO productOptionCombinationDTO = productOptionCombination.toDTO();
            productOptionCombinationDTOS.add(productOptionCombinationDTO);
        }
        log.info("여기4!!"+productOptionCombinationDTOS);

        productDTO.setOptionCombinations(productOptionCombinationDTOS);
        // Map Product Files
        List<ProductFileDTO> productFileDTOs = product.getFiles().stream()
                .map(file -> modelMapper.map(file, ProductFileDTO.class))
                .collect(Collectors.toList());
        productDTO.setProductFiles(productFileDTOs);
        log.info("여기5!!"+productOptionCombinationDTOS);


        // Map Reviews and Review Files
        List<ReviewDTO> reviewDTOs = product.getReviews().stream().map(review -> {
            ReviewDTO reviewDTO = review.ToDTO(review);
            return reviewDTO;
        }).collect(Collectors.toList());
        productDTO.setReviewDTOs(reviewDTOs);


        // Filter Specific File Descriptions
        List<String> filedesc = productFileDTOs.stream()
                .filter(file -> "940".equals(file.getType()))
                .map(ProductFileDTO::getSName)
                .collect(Collectors.toList());
        productDTO.setFiledesc(filedesc);



      // Set Seller Information
        SellerDTO sellerDTO = sellerService.getSeller(product.getSellerId());
        productDTO.setSeller(sellerDTO);

        log.info("view!!!!!!!!!!!!!!!!!!1; "+productDTO);


        return productDTO;
    }


        public void isSaleProduct() {}
}
