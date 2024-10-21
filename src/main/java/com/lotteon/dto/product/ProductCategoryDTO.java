package com.lotteon.dto.product;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class ProductCategoryDTO {

    private long id;
    private long parent_id;
    private String name;
    private int level;
    private String subcategory;
    private String disp_yn; //디스플레이 유무
    private String note;


}
