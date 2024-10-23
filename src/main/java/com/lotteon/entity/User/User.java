package com.lotteon.entity.User;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

@Entity
@Builder
@Getter
@Setter
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
    @JsonIgnore
    private Seller seller; // User와 Seller의 관계


    public enum Role {
        MEMBER, SELLER
    }



}
