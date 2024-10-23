package com.lotteon.dto.User;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDTO {

    @Id
    private long id;

    private String name;

    private String gender;

    private String email;

    private String hp;

    private String postcode;

    private String addr;

    private String addr2;

    private BigDecimal point;

    private String grade;

    private String user;

    private LocalDate regDate;


}
