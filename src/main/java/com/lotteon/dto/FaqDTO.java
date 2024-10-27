package com.lotteon.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqDTO {

    private int faqNo;
    private Long cate;
    private String faqtitle;
    private String faqcontent;
    private int faqhit;
    private String date;
}

