package com.lotteon.dto.product;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
@Builder
public class PageRequestDTO {
    @Builder.Default
    private int no=1;

    @Builder.Default
    private int page=1;

    @Builder.Default
    private int size=10;

    //검색
    private String type;
    private String keyword;

    //
    private String uid;


    public Pageable getPageable(String sort, int size) {
        this.size=size;
        return PageRequest.of(this.page-1,this.size, Sort.by(sort).descending());
    }

}
