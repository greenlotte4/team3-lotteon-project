package com.lotteon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
@Entity
@ToString
@Setter
public class QnA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int qna_id;
    private String qna_type1;
    private String qna_type2;
    private String qna_title;
    private String qna_writer;
    private String qna_content;

    @CreationTimestamp
    private LocalDateTime rdate;

    private String iscompleted;
    private int sellerid;
    private int productid;

}
