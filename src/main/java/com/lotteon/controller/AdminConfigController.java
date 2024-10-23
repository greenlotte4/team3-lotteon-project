package com.lotteon.controller;


import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.dto.VersionDTO;
import com.lotteon.repository.FooterInfoRepository;
import com.lotteon.service.AdminService;
import com.lotteon.service.FileService;
import com.lotteon.service.FooterInfoService;
import com.lotteon.service.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/config")
public class AdminConfigController {

    private final AdminService adminService;
    private final FileService fileService;
    private final FooterInfoService footerInfoService;
    private final FooterInfoRepository footerInfoRepository;
    private final VersionService versionService;

//    @GetMapping("/banner")
//    public String adminBanner(Model model) {
//        model.addAttribute("cate", "config");
//        model.addAttribute("content", "banner");
//        return "content/admin/config/admin_Banner";
//    }

    @GetMapping("/basic")
    public String adminBasic(Model model) {
        model.addAttribute("cate", "config");
        model.addAttribute("content", "basic");
        return "content/admin/config/admin_basic";
    }

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

    @PostMapping("/footerInfo")
    public ResponseEntity<Void> saveFooterInfo(@RequestBody FooterInfoDTO footerInfo) {
        // footerInfo를 데이터베이스에 저장하는 로직
        footerInfoService.saveFooterInfo(footerInfo);
        return ResponseEntity.ok().build();
    }

}
