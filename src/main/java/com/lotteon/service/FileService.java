package com.lotteon.service;

import com.lotteon.dto.BannerDTO;
import com.lotteon.repository.BannerRepository;
import com.lotteon.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;

    private final ModelMapper modelMapper;
    private final BannerRepository bannerRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    public BannerDTO uploadFile(BannerDTO bannerDTO) {

        // 파일 업로드 경로 파일 객체 생성
        File fileUploadPath = new File(uploadPath);

        // 파일 업로드 디렉터리가 존재하지 않으면 디렉터리 생성
        if (!fileUploadPath.exists()) {
            fileUploadPath.mkdirs();
        }

        // 파일 업로드 시스템 경로 구하기
        String path = fileUploadPath.getAbsolutePath();

        // 파일 정보 객체 가져오기
        MultipartFile file = bannerDTO.getFile(); // 배너 DTO에서 파일 정보 가져오기

        BannerDTO newBannerDTO = new BannerDTO();

            if (!file.isEmpty()) {
                String oName = file.getOriginalFilename();
                String ext = oName.substring(oName.lastIndexOf("."));
                String sName = UUID.randomUUID().toString() + ext;


                // 허용된 확장자 목록
                List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png");

                // 확장자가 허용된 목록에 있는지 확인
                if (!allowedExtensions.contains(ext)) {
                    throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. JPG, JPEG, PNG만 업로드할 수 있습니다.");
                }

                // 파일 저장
                try {
                    file.transferTo(new File(path, sName));
                } catch (IOException e) {
                    log.error(e);
                }
                newBannerDTO.setBan_oname(oName);
                newBannerDTO.setBan_image(sName);


            }
        return newBannerDTO;
    }

}
