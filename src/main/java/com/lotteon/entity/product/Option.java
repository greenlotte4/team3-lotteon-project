package com.lotteon.entity.product;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
@Entity
@Table(name="product_option")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private long parent_id;    //부모상품
    private String optionName; //프로덕트 code
    private String optionDesc;
    private int optionStock;
    private String optionCode;
    private String parentCode;
    @PostPersist
    public void generateOptionCode(){
        this.optionCode = parent_id+optionName;
    }

}
