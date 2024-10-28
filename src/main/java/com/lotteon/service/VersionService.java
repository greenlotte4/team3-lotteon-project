package com.lotteon.service;

import com.lotteon.dto.VersionDTO;
import com.lotteon.dto.admin.HeaderInfoDTO;
import com.lotteon.entity.Version;
import com.lotteon.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class VersionService {

    private final VersionRepository versionRepository;
    private final ModelMapper modelMapper;


    public void insertVersion(VersionDTO versionDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // 현재 로그인된 사용자 이름

        // `ver_writer`에 현재 사용자 이름 설정
        versionDTO.setVer_writer(currentUsername);

        versionRepository.save(modelMapper.map(versionDTO, Version.class));

    }
    public List<VersionDTO> getAllVersions() {
        return versionRepository.findAll().stream()
                .map(version -> {
                    VersionDTO dto = modelMapper.map(version, VersionDTO.class);
                    dto.setRdate(version.getRdate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void deleteCheck(List<Integer> data){
        for (Integer id : data) {
            versionRepository.deleteById(id);
        }
    }

    @Cacheable(value = "Version", key = "1")  // Cache with key "1" assuming a single HeaderInfo entry
    public VersionDTO getLatestVersion() {
        log.info("Fetching Version from the database.");
        Version version = versionRepository.findTopByOrderByVerIdDesc().orElse(null);

        return version != null ? modelMapper.map(version, VersionDTO.class) : null;
    }
}
