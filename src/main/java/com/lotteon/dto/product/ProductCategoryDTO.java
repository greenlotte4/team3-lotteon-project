package com.lotteon.dto.product;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class ProductCategoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long parent_id;
    private String name;
    private int level;
    private String subcategory;
    private String disp_yn; //디스플레이 유무
    private String note;


    private List<ProductCategoryDTO> children;  // 추가된 필드



}
