package com.lotteon.entity.product;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
/*
    이름 : 최영진
    날짜 : 2024-10-29
    @JsonInclude(JsonInclude.Include.NON_NULL) 추가

*/
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
@Entity
@Table(name="product_option")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String optionName; //프로덕트 code
    private String optionDesc;
    private long optionStock;
    private String optionCode;
    private String parentCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id")
    private OptionGroup optionGroup;
    private String name;
    private int additionalPrice;

    @PostPersist
    public void generateOptionCode(){
        this.optionCode = parentCode+optionName;
    }

    public void setStock(long stock){
        this.optionStock = stock;
    }

}
