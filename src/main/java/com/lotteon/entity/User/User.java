package com.lotteon.entity.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name="USER")
public class User{

    @Id
    private String uid;

    private String pass;

    @Enumerated(EnumType.STRING)
    private Role role; // enum 사용


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL) // Member와의 관계
    private Member member; // User와 Member의 관계

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL) // Seller와의 관계
    private Seller seller; // User와 Seller의 관계

    public enum Role {
        MEMBER, SELLER
    }



}
