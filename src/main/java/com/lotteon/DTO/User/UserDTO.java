package com.lotteon.DTO.User;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String uid;
    private String pass;

    @Builder.Default
    private String role = "USER";


}