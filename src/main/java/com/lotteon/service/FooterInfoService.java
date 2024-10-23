package com.lotteon.service;

import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.entity.FooterInfo;
import com.lotteon.repository.FooterInfoRepository;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class FooterInfoService {
    private final FooterInfoRepository footerInfoRepository;
    private final ModelMapper modelMapper;

    public void insertFooterInfo(FooterInfoDTO footerInfoDTO) {

        footerInfoRepository.save(modelMapper.map(footerInfoDTO, FooterInfo.class));

    }

}
