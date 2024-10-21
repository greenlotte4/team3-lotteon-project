package com.lotteon.controller;

import com.lotteon.dto.FooterInfoDTO;
import com.lotteon.dto.VersionDTO;
import com.lotteon.service.FooterInfoService;
import com.lotteon.service.VersionService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

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
