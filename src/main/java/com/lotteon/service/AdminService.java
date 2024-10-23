package com.lotteon.service;

import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.repository.BannerRepository;
import com.lotteon.entity.Banner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {

    private final BannerRepository bannerRepository;
    private final ModelMapper modelMapper;

    public Banner insertBanner(BannerDTO bannerDTO){
        Banner banner = bannerRepository.save(modelMapper.map(bannerDTO, Banner.class));
        return banner;
    }
    public List<BannerDTO> selectAllbanner(){
        List<Banner> banners = bannerRepository.findAll();
        List<BannerDTO> bannerDTOs = new ArrayList<>();
        for (Banner banner : banners) {
            BannerDTO bannerDTO = modelMapper.map(banner, BannerDTO.class);
            bannerDTOs.add(bannerDTO);
        }

        return bannerDTOs;
    }

    public void deleteBanner(List<Integer> data){
        for (Integer id : data) {
            bannerRepository.deleteById(id);
        }
    }

}
