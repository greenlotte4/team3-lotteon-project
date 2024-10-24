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

    private List<ProductListDTO> productDTOList;

    private String type;
    private int pg;
    private int size;
    private String sellerId;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev,next;

    @Builder
    public ProductListPageResponseDTO(PageRequestDTO pageRequestDTO,List<ProductListDTO> productDTOList,int total) {

        this.type = pageRequestDTO.getType();
        this.pg = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.productDTOList = new ArrayList<>();
        for(ProductListDTO productDTO : productDTOList){
            List<ProductFileDTO> productFileDTOLists = productDTO.getProductFiles();
            List<String> fileDesc = productDTO.getFiledesc();
            for(ProductFileDTO productFileDTO : productFileDTOLists){
                if(productFileDTO.getType().equals("190")){
                    productDTO.setFile190(productFileDTO.getSName());
                }else if(productFileDTO.getType().equals("230")){
                    productDTO.setFile230(productFileDTO.getSName());
                }else if(productFileDTO.getType().equals("456")){
                    productDTO.setFile456(productFileDTO.getSName());
                }else if(productFileDTO.getType().equals("940")){
                    fileDesc.add(productFileDTO.getSName());
                }

            }
            productDTO.setFiledesc(fileDesc);
            this.productDTOList.add(productDTO);
        }





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
