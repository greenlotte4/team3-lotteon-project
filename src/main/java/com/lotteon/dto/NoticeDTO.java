package com.lotteon.dto;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NoticeDTO {


    private Long noticeNo;
    private String noticetitle;
    private String noticetype;
    private String noticecontent;
    private String date;
    private int noticehit;
}


