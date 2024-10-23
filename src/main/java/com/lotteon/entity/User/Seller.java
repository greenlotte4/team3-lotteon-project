package com.lotteon.entity.User;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@ToString(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Seller")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @CreationTimestamp
    private LocalDateTime regDate;

    @OneToOne
    @JoinColumn(name = "user_uid") // 외래 키
    @JsonIgnore
    private User user; // User와의 관계

}
