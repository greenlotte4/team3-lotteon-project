package com.lotteon.service.product;

import com.lotteon.dto.product.ProductFileDTO;
import com.lotteon.entity.product.ProductFile;
import com.lotteon.repository.product.ProductFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductFileService {

    private final ProductFileRepository productFileRepository;

    private final ModelMapper modelMapper;

    @Value("spring.servlet.multipart.location")
    private String uploadPath;

    public List<ProductFileDTO> uploadFile(ProductFileDTO productFileDTO) {
        //파일 시스템 경로 구하기
        File fileuploadpath = new File(uploadPath+"productImg/");
        if(!fileuploadpath.exists()){
            fileuploadpath.mkdirs();
        }
        String path=  fileuploadpath.getAbsolutePath();
//        List<MultipartFile> files;
//        List<ProductFileDTO> fileDTOs = new ArrayList<>();
//
//        for(MultipartFile file : files){
//            log.info("file resource " + file.getResource());
//            String OName = file.getOriginalFilename();
//            if(OName != null && !OName.isEmpty()){
//
//                log.info("original name:"+OName);
//                //확장자
//                String ext = OName.substring(OName.lastIndexOf("."));
//                String Sname= UUID.randomUUID().toString()+ext;
//
//                //파일 저장
//                try {
//                    file.transferTo(new File(path,Sname));
//
//                    ProductFileDTO productFileDTO = ProductFileDTO.builder()
//                            .sName(Sname)
//                            .build();
//
//
//
//                } catch (IOException e) {
//                    log.error(e);
//                }
//
//
//
//            }

        return null;

    }


}
