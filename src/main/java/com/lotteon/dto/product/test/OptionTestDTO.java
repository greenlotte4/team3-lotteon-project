package com.lotteon.dto.product.test;


import com.lotteon.dto.product.OptionGroupDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class OptionTestDTO {

    private long optionId;
    private long parent_id;    //부모상품
    private String optionName;     //프로덕트 서브네임
    private String optionDesc;
    private long optionStock;
    private String OptionCode;
    private String parentCode;

    private OptionGroupDTO optionGroup;
    private String name;
    private int additionalPrice;


}
