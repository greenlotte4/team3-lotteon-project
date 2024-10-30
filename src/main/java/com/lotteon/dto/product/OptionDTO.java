package com.lotteon.dto.product;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class OptionDTO {

    private long id;
    private long parent_id;    //부모상품
    private String optionName;     //프로덕트 서브네임
    private String optionDesc;
    private long optionStock;
    private String OptionCode;
    private String parentCode;

}
