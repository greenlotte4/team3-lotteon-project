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
@Setter
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
    private int optionStock;
    private String optionCode;
    private String parentCode;

    @PostPersist
    public void generateOptionCode(){
        this.optionCode = parentCode+optionName;
    }

}
