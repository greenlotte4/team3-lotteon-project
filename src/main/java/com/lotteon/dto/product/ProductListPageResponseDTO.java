package com.lotteon.dto.product;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductListPageResponseDTO {

    private List<ProductDTO> productDTOs;
    private List<ProductSummaryDTO> productSummaryDTOs;
    private String type;
    private int pg;
    private int size;
    private String sellerId;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev,next;


    @Builder
    public ProductListPageResponseDTO(PageRequestDTO pageRequestDTO, List<ProductSummaryDTO> productSummaryDTOs, List<ProductDTO> ProductDTOs, int total) {

        this.type = pageRequestDTO.getType();
        this.pg = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;

        // `ProductDTOs`가 `null`일 경우 빈 리스트로 초기화
        this.productDTOs = (ProductDTOs == null || ProductDTOs.isEmpty()) ? new ArrayList<>() : ProductDTOs;

        // `productSummaryDTOs`가 비어 있지 않으면 초기화
        if (productSummaryDTOs != null && !productSummaryDTOs.isEmpty()) {
            this.productSummaryDTOs = productSummaryDTOs;
        } else {
            this.productSummaryDTOs = new ArrayList<>();
        }

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / (double) size)) * size;
        this.start = this.end - size - 1;

        int last = (int) (Math.ceil(total / (double) size)) * size;

        this.end = Math.min(end, last);
        if (this.start > this.end) {
            this.end = this.start;
        }

        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}
