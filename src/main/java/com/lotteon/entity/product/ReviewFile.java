package com.lotteon.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
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
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id") // 외래 키 설정
    private Review review; // Review와의 관계 설정


}
