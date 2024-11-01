package com.lotteon.entity.product;


import com.lotteon.entity.product.test.OptionTest;
import com.lotteon.entity.product.test.ProductTest;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name="optionGroup")
public class OptionGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long optionGroupId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String name;
    private boolean isRequired;

    @OneToMany(mappedBy = "optionGroup")
    private List<Option> options;

}
