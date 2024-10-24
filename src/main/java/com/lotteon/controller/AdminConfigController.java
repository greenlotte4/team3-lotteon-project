package com.lotteon.controller;


import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.dto.VersionDTO;
import com.lotteon.dto.admin.BannerDTO;
import com.lotteon.dto.admin.HeaderInfoDTO;
import com.lotteon.entity.Banner;
import com.lotteon.entity.FooterInfo;
import com.lotteon.entity.HeaderInfo;
import com.lotteon.repository.FooterInfoRepository;
import com.lotteon.repository.HeaderInfoRepository;
import com.lotteon.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/config")
public class AdminConfigController {

    private final AdminService adminService;
    private final FileService fileService;
    private final FooterInfoService footerInfoService;
    private final FooterInfoRepository footerInfoRepository;
    private final HeaderInfoService headerInfoService;
    private final HeaderInfoRepository headerInfoRepository;
    private final VersionService versionService;

//    @GetMapping("/banner")
//    public String adminBanner(Model model) {
//        model.addAttribute("cate", "config");
//        model.addAttribute("content", "banner");
//        return "content/admin/config/admin_Banner";
//    }

    @GetMapping("/terms")
    public String adminTerms(Model model) {
        model.addAttribute("cate", "config");
        model.addAttribute("content", "terms");
        return "content/admin/config/admin_Terms";
    }

    @GetMapping("/version")
    public String adminVersion(Model model) {
        model.addAttribute("cate", "config");
        model.addAttribute("content", "version");
        return "content/admin/config/admin_Version";
    }

    @ResponseBody
    @PostMapping("/version")
    public ResponseEntity<String> insertVersion(Model model, @RequestBody VersionDTO versionDTO) {
        versionService.insertVersion(versionDTO);
        return ResponseEntity.ok("{\"message\":\"버전이 등록되었습니다.\"}"); // JSON 응답으로 변경
    }

    @GetMapping("/basic")
    public String adminBasic(Model model) {
        model.addAttribute("cate", "config");
        model.addAttribute("content", "basic");

        FooterInfo footerInfo = footerInfoService.getFooterInfo();
        HeaderInfo headerInfo = headerInfoService.getHeaderInfo();

        // 조회한 정보를 모델에 추가
        model.addAttribute("headerInfo", headerInfo);
        model.addAttribute("footerInfo", footerInfo);
        return "content/admin/config/admin_basic";
    }


    @PostMapping("/footerInfo")
    public ResponseEntity<Void> saveFooterInfo(@RequestBody FooterInfoDTO footerInfo) {

        System.out.println("Received ID: " + footerInfo.getFt_id());
        if (footerInfo.getFt_id() != null) { // ID가 존재하면 업데이트 로직
            boolean exists = footerInfoService.existsById(footerInfo.getFt_id());
            if (exists) {
                // 데이터가 존재하면 업데이트
                footerInfoService.updateFooterInfo(footerInfo);
            } else {
                // ID가 있지만 데이터베이스에 존재하지 않으면 삽입
                footerInfoService.saveFooterInfo(footerInfo);
            }
        } else {
            // ID가 없으면 새로운 데이터로 간주하고 삽입
            footerInfoService.saveFooterInfo(footerInfo);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/headerInfo")
    public ResponseEntity<Void> saveHeaderInfo(@RequestBody HeaderInfoDTO headerInfo) {

        if (headerInfo.getHd_id() != null) {
            boolean exists = headerInfoService.existsById(headerInfo.getHd_id());
            if (exists) {
                // 데이터가 존재하면 업데이트
                headerInfoService.updateHeaderInfo(headerInfo);

            } else {
                // ID가 있지만 데이터베이스에 존재하지 않으면 삽입
                headerInfoService.saveHeaderInfo(headerInfo);
            }
        } else {
            // ID가 없으면 새로운 데이터로 간주하고 삽입
            headerInfoService.saveHeaderInfo(headerInfo);
        }

        return ResponseEntity.ok().build();

    }

}
