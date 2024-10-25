package com.lotteon.dto.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
public class ProductFileDTO {



    private int p_fno;
    private String sName;
    private String type;  //사이즈

    public ProductFileDTO(int p_fno, String sName, String type) {
        this.p_fno = p_fno;
        this.sName = sName;
        this.type = type;
    }


}
