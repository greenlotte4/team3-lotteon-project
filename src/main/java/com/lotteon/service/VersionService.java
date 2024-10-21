package com.lotteon.service;

import com.lotteon.dto.VersionDTO;
import com.lotteon.entity.Version;
import com.lotteon.repository.VersionRepository;
import groovy.util.logging.Log4j2;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Log4j2
@RequiredArgsConstructor
@Service
public class VersionService {

    private final VersionRepository versionRepository;
    private final ModelMapper modelMapper;


    public void insertVersion(VersionDTO versionDTO) {

        versionRepository.save(modelMapper.map(versionDTO, Version.class));

    }
}
