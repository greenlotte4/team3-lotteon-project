package com.lotteon.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/policy")
public class PolicyController {

    @GetMapping("/buyer")
    public String buyerPolicy(Model model) {
        model.addAttribute("content", "buyer");
        return "content/policy/buyer"; // Points to the "content/policy/buyer" template
    }

    @GetMapping("/seller")
    public String sellerPolicy(Model model) {
        model.addAttribute("content", "seller");
        return "content/policy/seller"; // Points to the "content/policy/seller" template
    }

    @GetMapping("/location")
    public String locationPolicy(Model model) {
        model.addAttribute("content", "location");
        return "content/policy/location"; // Points to the "content/policy/location" template
    }

    @GetMapping("/privacy")
    public String privacyPolicy(Model model) {
        model.addAttribute("content", "privacy");
        return "content/policy/privacy"; // Points to the "content/policy/privacy" template
    }

    @GetMapping("/finance")
    public String financePolicy(Model model) {
        model.addAttribute("content", "finance");
        return "content/policy/finance"; // Points to the "content/policy/finance" template
    }

}
