package com.lotteon.dto.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductFileDTO {



    private int p_fno;
    private String sName;
    private String type;  //사이즈
    private long product_id;




}
