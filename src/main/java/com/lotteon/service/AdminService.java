package com.lotteon.service;


import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.repository.BannerRepository;
import com.lotteon.entity.Banner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<BannerDTO> getActiveBanners() {
        LocalDateTime now = LocalDateTime.now();

        return bannerRepository.findAll().stream()
                .filter(banner -> {
                    // 배너가 표시될 날짜 범위를 설정
                    LocalDate startDate = LocalDate.parse(banner.getBan_sdate());
                    LocalDate endDate = LocalDate.parse(banner.getBan_edate());

                    // 배너가 표시될 시간 범위를 설정
                    LocalTime startTime = LocalTime.parse(banner.getBan_stime());
                    LocalTime endTime = LocalTime.parse(banner.getBan_etime());

                    // 현재 날짜가 시작 날짜와 종료 날짜 사이에 있는지 확인
                    boolean isDateInRange = !now.toLocalDate().isBefore(startDate) && !now.toLocalDate().isAfter(endDate);

                    // 현재 시간이 지정된 시간대에 있는지 확인
                    boolean isTimeInRange = (now.toLocalTime().isAfter(startTime) || now.toLocalTime().equals(startTime)) &&
                            (now.toLocalTime().isBefore(endTime) || now.toLocalTime().equals(endTime));

                    return isDateInRange && isTimeInRange;
                })
                .map(banner -> modelMapper.map(banner, BannerDTO.class))
                .collect(Collectors.toList());
    }


    public void deleteCheck(List<Integer> data){
        for (Integer id : data) {
            bannerRepository.deleteById(id);
        }
    }

}
