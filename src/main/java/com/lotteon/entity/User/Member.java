package com.lotteon.entity.User;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lotteon.dto.User.Grade;
import com.lotteon.dto.User.MemberDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
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

    private BigDecimal point = BigDecimal.ZERO;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore // 이 필드는 JSON 직렬화에서 제외됨
    private List<Point> points = new ArrayList<>();

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Grade grade= Grade.FAMILY;

    private String userinfocol;

    @CreationTimestamp
    private LocalDateTime regDate;

    @CreationTimestamp
    private LocalDateTime lastDate;

    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVE;

    public enum MemberStatus {
        ACTIVE("정상"),
        SUSPENDED("중지"),
        DORMANT("휴면"),
        WITHDRAWN("탈퇴");

        private final String displayName;

        MemberStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        //'VVIP','VIP','GOLD','SILVER','FAMILY'
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_uid") // 외래 키
    @JsonBackReference
    private User user; // User와의 관계


    private long totalOrder;

    public String getUid() {
        return user != null ? user.getUid() : null; // User가 null이 아닐 경우 uid 반환
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @JsonIgnore // 이 필드는 JSON 직렬화에서 제외됨
    private List<Delivery> deliveryList = new ArrayList<>();

    public void addDelivery(Delivery delivery) {
        deliveryList.add(delivery);
        delivery.setMember(this);  // 연관 관계 설정
    }

}
