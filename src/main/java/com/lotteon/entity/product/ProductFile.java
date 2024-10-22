package com.lotteon.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="productFile")
public class ProductFile {


    @Id
    private int p_fno;
    private String sName;
    private String type;  //사이즈



}
