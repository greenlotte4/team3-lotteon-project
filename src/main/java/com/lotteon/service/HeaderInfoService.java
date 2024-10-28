package com.lotteon.service;

import com.lotteon.dto.admin.HeaderInfoDTO;
import com.lotteon.entity.FooterInfo;
import com.lotteon.entity.HeaderInfo;
import com.lotteon.repository.HeaderInfoRepository;
import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value = "headerInfo", key = "1")
    public void updateHeaderInfo(HeaderInfoDTO headerInfoDTO){
        HeaderInfo entity = headerInfoRepository.findById(headerInfoDTO.getHd_id()).orElseThrow(() -> new RuntimeException("FooterInfo not found"));

        entity.setHd_title(headerInfoDTO.getHd_title());
        entity.setHd_subtitle(headerInfoDTO.getHd_subtitle());

        headerInfoRepository.save(entity);
        log.info("Updated HeaderInfo and evicted cache.");

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


    @Cacheable(value = "headerInfo", key = "1")  // Cache with key "1" assuming a single HeaderInfo entry
    public HeaderInfoDTO  getHeaderInfo() {
        log.info("Fetching HeaderInfo from the database.");
        HeaderInfo headerInfo = headerInfoRepository.findById(1L).orElse(null);
        return headerInfo != null ? modelMapper.map(headerInfo, HeaderInfoDTO.class) : null;
    }


}
