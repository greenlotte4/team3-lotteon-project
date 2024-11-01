package com.lotteon.dto.product;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OptionGroupDTO {

    private long optionGroupId;
    private Long  productId;
    private String name;
    private boolean isRequired;

//    @JsonManagedReference("product-options")
//    private ProductDTO product;
    private List<OptionDTO> items;

    // 새로운 필드 추가
    private String groupName;

}
