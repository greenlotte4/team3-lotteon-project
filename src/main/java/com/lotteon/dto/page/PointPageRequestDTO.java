package com.lotteon.dto.page;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PointPageRequestDTO {
    @Builder.Default
    private int no = 1; // 페이지 번호

    @Builder.Default
    private int pg = 1; // 페이지 번호 (기본값 1)

    private String type; // 요청 타입 (예: ADMIN, SELLER 등)

    @Builder.Default
    private int size = 10; // 페이지 크기 (기본값 10)

    private String cate;  // 카테고리
    private String keyword;  // 검색 키워드
    private LocalDate startDate;  // 시작 날짜
    private LocalDate endDate;    // 종료 날짜

    // 페이지 크기 기본값 설정
    public int getSize() {
        return this.size <= 0 ? 10 : this.size; // 0 이하일 경우 10으로 설정
    }

    // Pageable 객체로 변환
    public Pageable toPageable() {
        if (pg < 1) {
            pg = 1; // 최소값을 1로 설정
        }
        return PageRequest.of(pg - 1, getSize()); // 0부터 시작하므로 pg - 1
    }
}
