package com.lotteon.service;

import com.lotteon.dto.VersionDTO;
import com.lotteon.entity.Version;
import com.lotteon.repository.VersionRepository;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    public Version getLatestVersion() {
        return versionRepository.findTopByOrderByVerIdDesc()
                .orElse(null); // 최신 버전이 없으면 null 반환
    }
}
