package com.lotteon.entity.admin;


import com.lotteon.entity.User.Seller;
import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.Text;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int couponId;

    private String couponName;
    private String couponType;
    private String benefit; // 혜택
    private String startDate; // 시작 날짜
    private String endDate; // 종료 날짜
    private String Notes;

    private LocalDate rdate;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    private Seller seller;


}
