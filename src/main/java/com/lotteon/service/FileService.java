package com.lotteon.service;

import com.lotteon.dto.BannerDTO;
import com.lotteon.dto.FileDTO;
import com.lotteon.entity.FileEntity;
import com.lotteon.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;

    private final ModelMapper modelMapper;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    public List<FileDTO> uploadFile(BannerDTO bannerDTO) {

        // 파일 업로드 경로 파일 객체 생성
        File fileUploadPath = new File(uploadPath);

        // 파일 업로드 디렉터리가 존재하지 않으면 디렉터리 생성
        if (!fileUploadPath.exists()) {
            fileUploadPath.mkdirs();
        }

        // 파일 업로드 시스템 경로 구하기
        String path = fileUploadPath.getAbsolutePath();

        // 파일 정보 객체 가져오기
        List<MultipartFile> files = bannerDTO.getFiles(); // 배너 DTO에서 파일 정보 가져오기

        // 업로드 파일 정보 객체 리스트 생성
        List<FileDTO> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {

            if (!file.isEmpty()) {
                String oName = file.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf("."));
                String sName = UUID.randomUUID().toString() + ext;

                // 파일 저장
                try {
                    file.transferTo(new File(path, sName));
                } catch (IOException e) {
                    log.error(e);
                }

                FileDTO fileDTO = FileDTO.builder()
                        .oName(oName)
                        .sName(sName)
                        .filetype(bannerDTO.getBan_type())
                        .fileoption(bannerDTO.getBan_location())
                        .build();

                uploadedFiles.add(fileDTO);
            }
        }
        return uploadedFiles;
    }

    public void insertFile(FileDTO fileDTO) {
        FileEntity fileEntity = modelMapper.map(fileDTO, FileEntity.class);
        fileRepository.save(fileEntity);
    }
}
