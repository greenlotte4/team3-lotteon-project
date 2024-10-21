package com.lotteon.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDTO {

    private int fno;
    private String filetype;
    private String fileoption;
    private String oName;
    private String sName;
    private int download;
    private String rdate;

}