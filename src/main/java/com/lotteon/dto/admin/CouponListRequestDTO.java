package com.lotteon.dto.admin;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CouponListRequestDTO {
    @Builder.Default
    private int no = 1;

    private String type; // 요청 타입 (예: ADMIN, SELLER 등)


    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;
    private long sellerId; // 사용자 ID (선택적)
    private String grade; // 사용자 등급 (선택적)


    public Pageable getPageable() {
        return  PageRequest.of(page - 1, size); // Spring Data는 0부터 시작하므로 page - 1
    }
}
