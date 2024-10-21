package com.lotteon.service;

import com.lotteon.repository.BannerRepository;
import com.lotteon.dto.BannerDTO;
import com.lotteon.entity.Banner;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;

    public BannerDTO insertBanner(BannerDTO bannerDTO){

        bannerRepository.save(modelMapper.map(bannerDTO, Banner.class));

        return bannerDTO;
    }

}
