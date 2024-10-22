package com.lotteon.entity.User;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "user")
@Table(name="Member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String gender;

    private String email;

    private String hp;

    private String postcode;

    private String addr;

    private String addr2;

    private BigDecimal point ;

    private String grade;

    private String userinfocol;

    @CreationTimestamp
    private LocalDateTime regDate;

    @OneToOne
    @JoinColumn(name = "user_uid") // 외래 키
    private User user; // User와의 관계

}
