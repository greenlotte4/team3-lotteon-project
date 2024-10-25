package com.lotteon.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name="reviewFile")
public class ReviewFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fileId;
    private String sname;  // 파일 저장 이름 (업로드된 파일명)

}
