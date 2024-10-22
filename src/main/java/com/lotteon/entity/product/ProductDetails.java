package com.lotteon.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name="productDetails")
public class ProductDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int detailId;
    private Long productId;
    private String Productcondition;
    private String tax;
    private String receiptIssuance;
    private String busniesstype;
    private String manufactureImporter;
    private String manufactureCountry;
    private String handlingPrecautions;
    private String qualityWarranty;
    private String afterSalseManager;
    private String phoneNumber;
    private String shippingType;



}
