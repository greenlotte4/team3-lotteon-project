package com.lotteon.dto.admin;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.*;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CouponListRequestDTO {

    private String type; // 요청 타입 (예: ADMIN, SELLER 등)
    private int page; // 현재 페이지 번호
    private int size; // 페이지당 쿠폰 수
    private String uid; // 사용자 ID (선택적)
    private String grade; // 사용자 등급 (선택적)


    public Pageable getPageable() {
        return (Pageable) PageRequest.of(page - 1, size); // Spring Data는 0부터 시작하므로 page - 1
    }
}
