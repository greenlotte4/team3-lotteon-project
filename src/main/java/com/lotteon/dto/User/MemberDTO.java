package com.lotteon.dto.User;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MemberDTO {

    private long id;            // Member의 id
    private String name;        // Member의 name
    private String gender;      // Member의 gender
    private String email;       // Member의 email
    private String hp;          // Member의 hp
    private String postcode;    // Member의 postcode
    private String addr;        // Member의 addr
    private String addr2;       // Member의 addr2
    private BigDecimal point;   // Member의 point
    private String grade;       // Member의 grade
    private String uid;         // User의 uid
    private LocalDate regDate; // Member의 regDate
    private LocalDate lastDate;


    // 쿼리에서 사용하는 순서대로 생성자 작성
    public MemberDTO(long id, String name, String gender, String email, String hp, String postcode,
                     String addr, String addr2, BigDecimal point, String grade, String uid, LocalDate regDate, LocalDate lastDate) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.hp = hp;
        this.postcode = postcode;
        this.addr = addr;
        this.addr2 = addr2;
        this.point = point;
        this.grade = grade;
        this.uid = uid;
        this.regDate = regDate;
        this.lastDate = lastDate;
    }
}


