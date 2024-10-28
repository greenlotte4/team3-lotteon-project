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

    private String type;
    private int pg;
    private int size;
    private String sellerId;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev,next;


    @Builder
    public ProductListPageResponseDTO(PageRequestDTO pageRequestDTO,List<ProductDTO> productDTOs,int total) {

        this.type = pageRequestDTO.getType();
        this.pg = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.productDTOs = (productDTOs != null) ? productDTOs : new ArrayList<>();


        this.startNo = total - ((pg-1)*size);
        this.end=(int)(Math.ceil(this.pg/ (double)size))*size;
        this.start= this.end - size-1;

        int last=(int)(Math.ceil(total/(double)size))*size;

        this.end = end > last ? last : end;
        if(this.start>this.end){
            this.end=this.start;
        }

        this.prev = this.start> 1;
        this.next = total > this.end * this.size;

    }

}
