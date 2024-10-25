package com.lotteon.service;

import com.lotteon.dto.admin.HeaderInfoDTO;
import com.lotteon.entity.FooterInfo;
import com.lotteon.entity.HeaderInfo;
import com.lotteon.repository.HeaderInfoRepository;
import groovy.util.logging.Log;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Log4j2
@RequiredArgsConstructor
@Service
public class HeaderInfoService {

    private final HeaderInfoRepository headerInfoRepository;
    private final ModelMapper modelMapper;

    public boolean existsById(Long id){
        return headerInfoRepository.existsById(id);
    }

    public void saveHeaderInfo(HeaderInfoDTO headerInfoDTO){
        HeaderInfo headerInfo = HeaderInfo.builder()
                .hd_title(headerInfoDTO.getHd_title())
                .hd_subtitle(headerInfoDTO.getHd_subtitle())
                .build();


        headerInfoRepository.save(headerInfo);
    }

    public void updateHeaderInfo(HeaderInfoDTO headerInfoDTO){
        HeaderInfo entity = headerInfoRepository.findById(headerInfoDTO.getHd_id()).orElseThrow(() -> new RuntimeException("FooterInfo not found"));

        entity.setHd_title(headerInfoDTO.getHd_title());
        entity.setHd_subtitle(headerInfoDTO.getHd_subtitle());

        headerInfoRepository.save(entity);
    }

    public void saveHeaderInfo2(HeaderInfoDTO headerInfoDTO){
        HeaderInfo headerInfo = HeaderInfo.builder()
                .hd_sName1(headerInfoDTO.getHd_sName1())
                .hd_sName2(headerInfoDTO.getHd_sName2())
                .hd_sName3(headerInfoDTO.getHd_sName3())
                .hd_oName1(headerInfoDTO.getHd_oName1())
                .hd_oName2(headerInfoDTO.getHd_oName2())
                .hd_oName3(headerInfoDTO.getHd_oName3())
                .build();


        headerInfoRepository.save(headerInfo);
    }
    public void updateHeaderInfo2(HeaderInfoDTO headerInfoDTO){
        HeaderInfo entity = headerInfoRepository.findById(headerInfoDTO.getHd_id()).orElseThrow(() -> new RuntimeException("FooterInfo not found"));

        entity.setHd_sName1(headerInfoDTO.getHd_sName1());
        entity.setHd_sName2(headerInfoDTO.getHd_sName2());
        entity.setHd_sName3(headerInfoDTO.getHd_sName3());
        entity.setHd_oName1(headerInfoDTO.getHd_oName1());
        entity.setHd_oName2(headerInfoDTO.getHd_oName2());
        entity.setHd_oName3(headerInfoDTO.getHd_oName3());

        headerInfoRepository.save(entity);
    }


    public HeaderInfo getHeaderInfo() {
        return headerInfoRepository.findById(1L)
                .orElse(null);  // 데이터가 없으면 null 반환
    }
}
