package com.lotteon.dto.User;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SellerDTO {

    @Id
    private Long id;

    private String  company;

    private String  ceo;

    private String  bno;

    private String  mo;

    private String hp;

    private String fax;

    private String addr;

    private String addr2;

    private String postcode;

    private String brand;

    private String grade;

    private LocalDateTime regDate;


}
