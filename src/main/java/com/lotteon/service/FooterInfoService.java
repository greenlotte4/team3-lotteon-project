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

    public void saveFooterInfo(FooterInfoDTO footerInfoDTO) {
        FooterInfo footerInfo = FooterInfo.builder()
                .ft_company(footerInfoDTO.getFt_company())
                .ft_ceo(footerInfoDTO.getFt_ceo())
                .ft_bo(footerInfoDTO.getFt_bo())
                .ft_mo(footerInfoDTO.getFt_mo())
                .ft_addr1(footerInfoDTO.getFt_addr1())
                .ft_addr2(footerInfoDTO.getFt_addr2())
                .ft_hp(footerInfoDTO.getFt_hp())
                .ft_time(footerInfoDTO.getFt_time())
                .ft_email(footerInfoDTO.getFt_email())
                .ft_troublehp(footerInfoDTO.getFt_troublehp())
                .ft_copyright(footerInfoDTO.getFt_copyright())
                .build();

        footerInfoRepository.save(footerInfo);
    }




}
