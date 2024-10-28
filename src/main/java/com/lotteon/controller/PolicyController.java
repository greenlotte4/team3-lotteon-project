package com.lotteon.controller;


import com.lotteon.dto.admin.TermsDto;
import com.lotteon.entity.admin.Terms;
import com.lotteon.service.admin.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.query.Term;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/policy")
public class PolicyController {

    private final TermsService termsService;

    @GetMapping("/buyer")
    public String buyerPolicy(Model model) {
        List<Terms> allTerms = termsService.findAllTerms();

        // 원하는 type들만 필터링
        List<Terms> buyerTerms = allTerms.stream()
                .filter(terms -> Objects.equals("BUYER", terms.getType()))
                .collect(Collectors.toList());

        model.addAttribute("termsList", buyerTerms); // 필터링된 termsList 추가
        model.addAttribute("content", "buyer");

        return "content/policy/buyer";
    }

    @GetMapping("/seller")
    public String sellerPolicy(Model model) {
        List<Terms> allTerms = termsService.findAllTerms();

        // "SELLER", "ELECTRONIC_FINANCE", "LOCATION_INFO", "PRIVACY_POLICY" 타입의 약관만 필터링
        List<Terms> sellerTerms = allTerms.stream()
                .filter(terms -> Objects.equals("SELLER", terms.getType()))
                .collect(Collectors.toList());

        model.addAttribute("termsList", sellerTerms);
        model.addAttribute("content", "seller");

        return "content/policy/seller";
    }

    @GetMapping("/location")
    public String locationPolicy(Model model) {
        List<Terms> allTerms = termsService.findAllTerms();

        // "BUYER", "ELECTRONIC_FINANCE", "LOCATION_INFO", "PRIVACY_POLICY" 타입의 약관만 필터링
        List<Terms> locationTerms = allTerms.stream()
                .filter(terms -> Objects.equals("LOCATION_INFO", terms.getType()))
                .collect(Collectors.toList());

        model.addAttribute("termsList", locationTerms);
        model.addAttribute("content", "location");

        return "content/policy/location";
    }

    @GetMapping("/privacy")
    public String privacyPolicy(Model model) {
        List<Terms> allTerms = termsService.findAllTerms();

        // "BUYER", "ELECTRONIC_FINANCE", "LOCATION_INFO", "PRIVACY_POLICY" 타입의 약관만 필터링
        List<Terms> privacyTerms = allTerms.stream()
                .filter(terms -> Objects.equals("PRIVACY_POLICY", terms.getType()))
                .collect(Collectors.toList());

        model.addAttribute("termsList", privacyTerms);
        model.addAttribute("content", "privacy");

        return "content/policy/privacy";
    }

    @GetMapping("/finance")
    public String financePolicy(Model model) {
        List<Terms> allTerms = termsService.findAllTerms();

        // "BUYER", "ELECTRONIC_FINANCE", "LOCATION_INFO", "PRIVACY_POLICY" 타입의 약관만 필터링
        List<Terms> financeTerms = allTerms.stream()
                .filter(terms ->Objects.equals("ELECTRONIC_FINANCE", terms.getType()))
                .collect(Collectors.toList());

        model.addAttribute("termsList", financeTerms);
        model.addAttribute("content", "finance");

        return "content/policy/finance";
    }
}



