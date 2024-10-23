package com.lotteon.dto;


import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Setter
public class QnaDTO {

    private int qna_id;
    private String qna_type1;
    private String qna_type2;
    private String qna_title;
    private String qna_writer;
    private String qna_content;
    private String rdate;
    private String iscompleted;
    private int sellerid;
    private int productid;
}


