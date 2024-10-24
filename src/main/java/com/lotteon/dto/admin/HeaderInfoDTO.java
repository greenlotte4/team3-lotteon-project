package com.lotteon.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeaderInfoDTO {

    private Long hd_id;

    private String hd_title;
    private String hd_subtitle;
    private String hd_sName1;
    private String hd_sName2;
    private String hd_sName3;

    private MultipartFile file;
}
