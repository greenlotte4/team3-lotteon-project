package com.lotteon.controller;


import com.lotteon.dto.BannerDTO;
import com.lotteon.dto.FileDTO;
import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.dto.VersionDTO;
import com.lotteon.service.AdminService;
import com.lotteon.service.FileService;
import com.lotteon.service.FooterInfoService;
import com.lotteon.service.VersionService;
import lombok.Builder;
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
    private final FooterInfoService footerInfoService;
    private final VersionService versionService;


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

    @PostMapping("/config/basic")
    public String FooterInfoModify(Model model, FooterInfoDTO footerInfoDTO) {

        footerInfoService.insertFooterInfo(footerInfoDTO);

        return "redirect:/admin/config/basic";
    }

    @ResponseBody
    @PostMapping("/config/version")
    public ResponseEntity<String> insertVersion(Model model, @RequestBody VersionDTO versionDTO) {
        versionService.insertVersion(versionDTO);
        return ResponseEntity.ok("{\"message\":\"버전이 등록되었습니다.\"}"); // JSON 응답으로 변경
    }
}
