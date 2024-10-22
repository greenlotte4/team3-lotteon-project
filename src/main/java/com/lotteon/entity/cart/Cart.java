package com.lotteon.entity.cart;

import com.lotteon.entity.User.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue
    private int cartId;

    @OneToOne
    @JoinColumn(name = "uid", nullable = false)
    private User user;

    private LocalDateTime rdate;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;
}
