package com.lotteon.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class BoardCateDTO {

    private Long boardCateId;
    private String name;
    private int level;
    private Long parentId;
}
