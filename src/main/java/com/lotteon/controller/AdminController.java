package com.lotteon.controller;

import com.lotteon.dto.BannerDTO;
import com.lotteon.dto.FileDTO;
import com.lotteon.service.AdminService;
import com.lotteon.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final FileService fileService;


    @GetMapping("/main")
    public String adminMain( Model model) {

        model.addAttribute("cate", "main");
        return "adminIndex";
    }

    @GetMapping("/{cate}/{content}")
    public String admin(@PathVariable String content, @PathVariable String cate, Model model) {

        model.addAttribute("cate", cate);
        model.addAttribute("content", content);
        log.info(content);
        log.info("cate"+cate);
        return "adminIndex";
    }
    @ResponseBody
    @PostMapping("/banner/upload")
    public ResponseEntity<String> banner(@ModelAttribute BannerDTO bannerDTO) {
        log.info("bannerDTO :" + bannerDTO);


        //파일 업로드
        List<FileDTO> uploadfiles = fileService.uploadFile(bannerDTO);

        //글저장
         BannerDTO banner = adminService.insertBanner(bannerDTO);

        if(!uploadfiles.isEmpty()) {
            FileDTO fileDTO = uploadfiles.get(0);
            fileDTO.setFiletype(banner.getBan_type());
            fileDTO.setFileoption(banner.getBan_location());
            fileService.insertFile(fileDTO);
        }
        return ResponseEntity.ok("배너 등록 성공");
    }



}
