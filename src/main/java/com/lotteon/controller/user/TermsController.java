package com.lotteon.controller.user;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class TermsController {

    @GetMapping("/terms")
    public String terms(@RequestParam String type, Model model) {
        if ("member".equals(type)) {
            // 개인 회원 약관 내용 설정
            model.addAttribute("termsContent", "개인 회원 약관 내용...");
        } else if ("seller".equals(type)) {
            // 셀러 회원 약관 내용 설정
            model.addAttribute("termsContent", "셀러 회원 약관 내용...");
        }
        return "terms"; // 약관 페이지 반환
    }


    @PostMapping("/terms")
    public String acceptTerms(
            @RequestParam String type,
            @RequestParam String someField,
            HttpSession session) {

        // 세션에 값 저장
        session.setAttribute("someField", someField);
        session.setAttribute("userType", type); // 회원 유형도 세션에 저장

        System.out.println("Received someField: " + someField);

        // 회원 유형에 따라 리다이렉트
        if ("member".equals(type)) {
            return "redirect:/user/memberregister"; // 개인 회원가입 폼으로 리다이렉트
        } else if ("seller".equals(type)) {
            return "redirect:/user/sellerregister"; // 셀러 회원가입 폼으로 리다이렉트
        }

        return "redirect:/signup"; // 기본값 (오류 처리용)
    }

}
