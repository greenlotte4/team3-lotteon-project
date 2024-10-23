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
    private String faqtype1;
    private String faqtype2;
    private String faqtitle;
    private String faqcontent;
    private int faqhit;
    private String date;
}

