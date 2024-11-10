package com.lotteon.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lotteon.config.RedisConfig;
import com.lotteon.dto.product.PageRequestDTO;
import com.lotteon.dto.product.ProductListPageResponseDTO;
import com.lotteon.dto.product.ProductSummaryDTO;
import com.lotteon.service.product.ProductService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Controller
public class SearchController {

    private final ProductService productService;
    private final RedisTemplate<String,Object>  redisTemplate;

    private final ObjectMapper objectMapper;


    @ResponseBody
    @GetMapping("/api/search")
    public ProductListPageResponseDTO marketHeaderSearch(
            @RequestParam String query,
            @RequestParam(required = false) String sort
            ,PageRequestDTO pageRequestDTO
            ,Model model) {
        log.info("query!!!2222"+query);
        log.info("Sort!!!!"+sort);


        pageRequestDTO.setType("productName");
        pageRequestDTO.setKeyword(query);
        pageRequestDTO.setSort(sort);

        ProductListPageResponseDTO responseDTO = productService.getSearchProductBySort(pageRequestDTO);
        log.info("확인!!!!!!!!"+responseDTO);






//        model.addAttribute("productPageResponseDTO", productPageResponseDTO);
////        model.addAttribute("query", query);
        return responseDTO;
    }


    @ResponseBody
    @GetMapping("/api/advanced-search")
    public ProductListPageResponseDTO advancedSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "false") boolean filterByName,
            @RequestParam(defaultValue = "false") boolean filterByDescription,
            @RequestParam(defaultValue = "false") boolean filterByPrice,
            @RequestParam(defaultValue = "exact") String searchMode,
            @RequestParam(defaultValue = "0") Integer minPrice,
            @RequestParam(defaultValue = "1000000000") Long maxPrice,  // 예시 최대 가격 값 변경
            PageRequestDTO pageRequestDTO) throws JsonProcessingException {

        log.info("query33333 "+query);
        log.info("query33333222 "+filterByName);
        log.info("query33333333 "+filterByDescription);


        // 캐시에서 검색 결과 가져오기
        String cacheKey = "search:" + query;

        // Redis에서 불러오기
        List<ProductSummaryDTO> cachedResults= getCachedResults(cacheKey);


        pageRequestDTO.setSearchMode(searchMode);
        // 캐시된 검색 결과가 있을 때 필터 적용
        if (cachedResults != null) {
            log.info("Cache hit for key: " + cacheKey);
            // 필터 조건 적용하여 리스트 필터링
            BooleanBuilder conditions = new BooleanBuilder();
            List<ProductSummaryDTO> filteredResults = cachedResults.stream()
                    .filter(product -> {
                        boolean matches = true;
                        // 상품명 필터
                        if (filterByName) {
                            matches = matches && product.getProductName().toLowerCase().contains(query.toLowerCase());
                        }
                        // 설명 필터
                        if (filterByDescription) {
                            matches = matches && product.getProductDesc().toLowerCase().contains(query.toLowerCase());
                        }
                        // 가격 범위 필터
                        if (filterByPrice) {
                            matches = matches && product.getPrice() >= minPrice && product.getPrice() <= maxPrice;
                        }

                        return matches;
                    })
                    .collect(Collectors.toList());

            return  ProductListPageResponseDTO.builder()
                    .productSummaryDTOs(filteredResults)
                    .pageRequestDTO(pageRequestDTO)
                    .total(filteredResults.size())
                    .build();
        }

        // 캐시에 검색 결과가 없을 경우 DB 조회
        pageRequestDTO.setKeyword(query);
        ProductListPageResponseDTO responseDTO = productService.getFilteredSearchProducts(
                pageRequestDTO, filterByName, filterByDescription, filterByPrice, minPrice, maxPrice);

        // 조회된 결과를 캐시에 저장
        redisTemplate.opsForValue().set(cacheKey, responseDTO.getProductSummaryDTOs(), 120, TimeUnit.SECONDS);

        return responseDTO;
    }

    @GetMapping("/market/search")
    public String marketSearch(    @RequestParam String query,PageRequestDTO pageRequestDTO,Model model) {
        log.info("query!!!"+query);

        pageRequestDTO.setType("productName");
        pageRequestDTO.setKeyword(query);
        pageRequestDTO.setSort("sold");

        ProductListPageResponseDTO responseDTO = productService.getSearchProductBySort(pageRequestDTO);
        log.info("확인!!!!!!!!"+responseDTO);

        model.addAttribute("products", responseDTO);
        model.addAttribute("query", query);

        model.addAttribute("content", "search");
        // 최초 검색 시 캐시 저장
        String cacheKey = "search:" + query;
        redisTemplate.opsForValue().set(cacheKey, responseDTO);
        redisTemplate.expire(cacheKey, 360, TimeUnit.SECONDS); // 캐시 만료 시간 설정
        return "content/market/marketSearch"; // Points to the "content/market/marketSearch" template
    }


    public List<ProductSummaryDTO> getCachedResults(String cacheKey) {
        // JSON 배열 문자열을 Redis에서 가져오기
        String jsonArray = (String) redisTemplate.opsForValue().get(cacheKey);

        // JSON 배열 문자열을 List<ProductSummaryDTO>로 변환
        List<ProductSummaryDTO> cachedResults = new ArrayList<>();
        if (jsonArray != null) {
            try {
                cachedResults = objectMapper.readValue(jsonArray, new TypeReference<List<ProductSummaryDTO>>() {});
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return cachedResults;
    }
}