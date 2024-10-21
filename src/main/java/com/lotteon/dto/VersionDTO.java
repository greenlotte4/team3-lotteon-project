package com.lotteon.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VersionDTO {

    private int ver_id;

    private String ver_name;
    private String ver_writer;

    private String rdate;

    private String ver_content;
}
