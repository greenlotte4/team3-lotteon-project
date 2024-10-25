package com.lotteon.dto.product;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReviewDTO {

    private long reviewId;
    private String writer;
    private LocalDateTime rdate;
    private String title;
    private String content;
    private String rating;  //상품평점
    private List<MultipartFile> pReviewFiles;

}
