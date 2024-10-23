package com.lotteon.controller;


import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.entity.Banner;
import com.lotteon.service.AdminService;
import com.lotteon.service.FileService;
import com.lotteon.service.FooterInfoService;
import com.lotteon.service.VersionService;
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

        return "content/admin/admin_index";
    }

    @ResponseBody
    @DeleteMapping("/banner/delete")
    public ResponseEntity<?> bannerDelete(@RequestBody List<Integer> data){
         if(data == null || data.isEmpty()){
             return ResponseEntity.badRequest().body("삭제할 항목이 없습니다.");
         }
         adminService.deleteBanner(data);
         return ResponseEntity.ok().build();
    }

    @GetMapping("/config/banner")
    public String bannerList(Model model) {
        List<BannerDTO> banners = adminService.selectAllbanner();
        log.info("gdgd :" + banners);
        model.addAttribute("banners", banners);
        return "content/admin/config/admin_Banner";
    }

    @ResponseBody
    @PostMapping("/banner/upload")
    public ResponseEntity<?> banner(@ModelAttribute BannerDTO bannerDTO , Model model) {
        log.info("bannerDTO :" + bannerDTO);


        //파일 업로드
        BannerDTO newBanner = fileService.uploadFile(bannerDTO);

        bannerDTO.setBan_oname(newBanner.getBan_oname()); // 원래 파일 이름 설정
        bannerDTO.setBan_image(newBanner.getBan_image()); // 저장된 파일 이름 설정

        //글저장
        Banner banner = adminService.insertBanner(bannerDTO);

        return ResponseEntity.ok().body(banner);
    }

    @PostMapping("/config/basic")
    public String FooterInfoModify(Model model, FooterInfoDTO footerInfoDTO) {

//        footerInfoService.insertFooterInfo(footerInfoDTO);

        return "redirect:/admin/config/basic";
    }

}
