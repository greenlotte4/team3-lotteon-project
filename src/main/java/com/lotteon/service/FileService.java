package com.lotteon.service;


import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.dto.product.ProductFileDTO;
import com.lotteon.repository.BannerRepository;
import com.lotteon.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

    //banner UploadFile
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

    //productFile upload;
    public List<ProductFileDTO> uploadFile(MultiValueMap<String, MultipartFile> images) {
        //파일 시스템 경로 구하기
        File fileuploadpath = new File(uploadPath+"productImg/");
        if(!fileuploadpath.exists()){
            fileuploadpath.mkdirs();
        }
        String path=  fileuploadpath.getAbsolutePath();
        List<ProductFileDTO> fileDTOs = new ArrayList<>();


        for(String key :images.keySet() ){
            List<MultipartFile> files = images.get(key);
            if (files != null) {
                for (MultipartFile file : files) {
                    String originalName = file.getOriginalFilename();
                    if (originalName != null && !originalName.isEmpty()) {
                        // 확장자 추출
                        String ext = originalName.substring(originalName.lastIndexOf("."));
                        // 저장될 파일 이름 생성 (UUID + 확장자)
                        String savedName = UUID.randomUUID().toString() + ext;

                        // 파일 저장
                        try {
                            file.transferTo(new File(path, savedName));

                            // 업로드된 파일 정보를 DTO로 변환하여 저장
                            ProductFileDTO productFileDTO = ProductFileDTO.builder()
                                    .sName(savedName)
                                    .type(key) // 파일의 MIME 타입 저장
                                    .build();

                            fileDTOs.add(productFileDTO); // 리스트에 추가
                        } catch (IOException e) {
                            log.error(e);
                            // 파일 저장 중 오류 발생 시 처리
                        }
                    }
                }
            }
        }
        log.info(fileDTOs);
        return fileDTOs;

    }


}
