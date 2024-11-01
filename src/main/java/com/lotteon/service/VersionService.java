package com.lotteon.service;

import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.dto.VersionDTO;
import com.lotteon.dto.admin.HeaderInfoDTO;
import com.lotteon.entity.Version;
import com.lotteon.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
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
    private final CacheManager cacheManager;


    @CacheEvict(value = "Version", allEntries = true)
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


    @CacheEvict(value = "Version", allEntries = true)
    public void refreshVersionCache() {
        getLatestVersion(); // 캐시를 초기화하기 위해 호출
    }
    @Scheduled(cron = "0 39 2 * * *") // 매일 10:10 AM에 실행
    public void scheduledVersionCacheUpdate() {
        refreshVersionCache();
    }

    @Cacheable(value = "Version", key = "1")  // Cache with key "1" assuming a single HeaderInfo entry
    public VersionDTO getLatestVersion() {
        log.info("Fetching Version from the database.");
        Version version = versionRepository.findTopByOrderByVerIdDesc().orElse(null);
        return version != null ? modelMapper.map(version, VersionDTO.class) : null;
    }


    public VersionDTO getVersionWithCacheCheck() {
        // 캐시 매니저에서 Version 캐시 가져오기
        Cache cache = cacheManager.getCache("Version");

        // 캐시에서 값을 안전하게 가져오기 (null 검사를 포함)
        Cache.ValueWrapper wrapper = cache != null ? cache.get(1) : null;
        VersionDTO cachedVersion = (wrapper != null && wrapper.get() instanceof VersionDTO) ? (VersionDTO) wrapper.get() : null;

        // 캐시에 없으면 DB에서 가져오고, 있으면 캐시된 값 반환
        if (cachedVersion == null) {
            log.info("Cache miss - Fetching Version from the database");
            return getLatestVersion(); // @Cacheable 메서드를 호출하여 캐시 갱신
        } else {
            log.info("Cache hit - Returning cached VersionDTO");
            return cachedVersion;
        }
    }
}
